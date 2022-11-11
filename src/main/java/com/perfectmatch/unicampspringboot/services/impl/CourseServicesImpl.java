package com.perfectmatch.unicampspringboot.services.impl;

import com.perfectmatch.unicampspringboot.db.CourseDao;
import com.perfectmatch.unicampspringboot.db.CourseRecDao;
import com.perfectmatch.unicampspringboot.db.FavoriteDao;
import com.perfectmatch.unicampspringboot.db.Prerequisite;
import com.perfectmatch.unicampspringboot.mapper.CourseMapper;
import com.perfectmatch.unicampspringboot.mapper.FavoriteMapper;
import com.perfectmatch.unicampspringboot.services.CourseServices;
import com.perfectmatch.unicampspringboot.utils.MyUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServicesImpl implements CourseServices {
    @Autowired
    CourseMapper courseMapper;

    @Autowired
    FavoriteMapper favoriteMapper;

    public CourseDao getCourseById(Long id) {
        return courseMapper.findCourseById(id);
    }

    public void addCourse(Long subcategory_id, String name, String provider,
                          String description, Integer difficulty, Integer est_hour,
                          String website, String video, String assignment) {
        courseMapper.insertCourse(subcategory_id.toString(), name, provider,
                description, difficulty.toString(),
                est_hour.toString(), website, video, assignment);
    }

    public void updateCourse(Long id, Long subcategory_id, String name, String provider,
                             String description, Integer difficulty, Integer est_hour,
                             String website, String video, String assignment) {
        courseMapper.updateCourse(id.toString(), subcategory_id.toString(), name,
                provider, description, difficulty.toString(),
                est_hour.toString(), website, video, assignment);
    }

    public void deleteCourse(Long id) {
        courseMapper.deleteCourse(id.toString());
    }

    public List<CourseDao> listCourse() {
        return courseMapper.listCourse();
    }

    public List<Long> getPreCourse(Long id) {
        return courseMapper.getPreCourse(id.toString());
    }


    public List<Long> getPostCourse(Long id) {
        return courseMapper.getPostCourse(id.toString());
    }

    public Prerequisite getPrerequisite(Long pre_id, Long post_id) {
        return courseMapper.getPrerequisite(pre_id.toString(), post_id.toString());
    }

    public void addPrerequisite(Long pre_id, Long post_id) {
        courseMapper.addPrerequisite(pre_id.toString(), post_id.toString());
    }

    public void deletePrerequisite(Long pre_id, Long post_id) {
        courseMapper.deletePrerequisite(pre_id.toString(), post_id.toString());
    }

    public void deleteRelatedPrerequisite(Long id) {
        courseMapper.deleteRelatedPrerequisite(id.toString());
    }

    public List<CourseRecDao> listNew() {
        return courseMapper.listNew();
    }

    public List<CourseRecDao> listHot() {
        return courseMapper.listHot();
    }

    public List<CourseRecDao> listRec(Long userId) throws TasteException {
        FastByIDMap<PreferenceArray> fastByIdMap = new FastByIDMap<>();
        List<FavoriteDao> favoriteDaoList = favoriteMapper.getAll();
        List<Long> courseIds = favoriteMapper.selectFavoriteByUserId(String.valueOf(userId));
        if (CollectionUtils.isEmpty(courseIds)) return Collections.emptyList();
        favoriteDaoList.stream()
                .collect(Collectors.groupingBy(FavoriteDao::getUser_id))
                .values()
                .stream()
                .map(favoriteDaos -> favoriteDaos.stream()
                        .map(favoriteDao -> new GenericPreference(favoriteDao.getUser_id(), favoriteDao.getCourse_id(), 5))
                        .toArray(GenericPreference[]::new)
                )
                .forEach(genericArrayPreference -> fastByIdMap.put(genericArrayPreference[0].getUserID(), new GenericUserPreferenceArray(Arrays.asList(genericArrayPreference))));
        DataModel dataModel = new GenericDataModel(fastByIdMap);
        UserSimilarity similarity = new EuclideanDistanceSimilarity(dataModel);
        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(100, similarity, dataModel);
        List<String> neighbors = Arrays.stream(userNeighborhood.getUserNeighborhood(userId)).boxed().map(String::valueOf).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(neighbors)) return Collections.emptyList();
        HashMap<Long, Integer> m = new HashMap<>();
        Map<Long, List<FavoriteDao>> mm = favoriteMapper.selectFavoriteByUserIds(neighbors).stream().collect(Collectors.groupingBy(FavoriteDao::getCourse_id));
        for(Long key : mm.keySet()){
            if(!courseIds.contains(key)){
                m.put(key,mm.get(key).size());
            }
        }
        LinkedHashMap<Long, Integer> mmm = MyUtils.sortMap(m);
        Iterator<Map.Entry<Long, Integer>> it=mmm.entrySet().iterator();
        int i = 0;
        List<String> ids = new ArrayList<>();
        while (it.hasNext() && i < 4){
            Map.Entry<Long, Integer> entry=it.next();
            ids.add(String.valueOf(entry.getKey()));
            i++;
        }
        if (CollectionUtils.isEmpty(ids)) return Collections.emptyList();
        return courseMapper.findByIds(ids);
    }

    public List<CourseRecDao> listRelated(Long courseId) throws TasteException {
        FastByIDMap<PreferenceArray> fastByIdMap = new FastByIDMap<>();
        List<FavoriteDao> favoriteDaoList = favoriteMapper.getAll();
        favoriteDaoList.stream()
                .collect(Collectors.groupingBy(FavoriteDao::getCourse_id))
                .values()
                .stream()
                .map(favoriteDaos -> favoriteDaos.stream()
                        .map(favoriteDao -> new GenericPreference(favoriteDao.getCourse_id(), favoriteDao.getUser_id(), 5))
                        .toArray(GenericPreference[]::new)
                )
                .forEach(genericArrayPreference -> fastByIdMap.put(genericArrayPreference[0].getUserID(), new GenericUserPreferenceArray(Arrays.asList(genericArrayPreference))));
        DataModel dataModel = new GenericDataModel(fastByIdMap);
        UserSimilarity similarity = new EuclideanDistanceSimilarity(dataModel);
        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(4, similarity, dataModel);
        List<String> ids = Arrays.stream(userNeighborhood.getUserNeighborhood(courseId)).boxed().map(String::valueOf).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) return Collections.emptyList();
        return courseMapper.findByIds(ids);
    }
}
