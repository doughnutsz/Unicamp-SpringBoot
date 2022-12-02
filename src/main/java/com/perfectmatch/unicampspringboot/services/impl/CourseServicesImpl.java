package com.perfectmatch.unicampspringboot.services.impl;

import com.perfectmatch.unicampspringboot.db.*;
import com.perfectmatch.unicampspringboot.mapper.CategoryMapper;
import com.perfectmatch.unicampspringboot.mapper.CourseMapper;
import com.perfectmatch.unicampspringboot.mapper.FavoriteMapper;
import com.perfectmatch.unicampspringboot.services.CourseServices;
import com.perfectmatch.unicampspringboot.services.GradeServices;
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

    @Autowired
    GradeServices gradeServices;

    @Autowired
    CategoryMapper categoryMapper;

    public CourseDao getCourseById(Long id) {
        return courseMapper.findCourseById(id);
    }

    public CourseDaoWithGrade getCourseWithGradeById(Long id) {
        CourseDao courseDao = courseMapper.findCourseById(id);
        CourseDaoWithGrade courseDaoWithGrade = new CourseDaoWithGrade(courseDao);
        courseDaoWithGrade.setRatings(gradeServices.getGradeDetail(id));
        return courseDaoWithGrade;
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

    public List<CourseRecDao> getPreCourse(Long id) {
        List<Long> ls = courseMapper.getPreCourse(id.toString());
        List<CourseRecDao> ret = new ArrayList<>();
        for (Long it : ls) {
            ret.add(new CourseRecDao(getCourseById(it)));
        }
        return ret;
    }


    public List<CourseRecDao> getPostCourse(Long id) {
        List<Long> ls = courseMapper.getPostCourse(id.toString());
        List<CourseRecDao> ret = new ArrayList<>();
        for (Long it : ls) {
            ret.add(new CourseRecDao(getCourseById(it)));
        }
        return ret;
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
        List<CourseRecDao> courseRecDaoList =  courseMapper.listNew();
        courseRecDaoList.forEach((CourseRecDao c) -> c.setRatings(gradeServices.getGradeDetail(c.getId())));
        return courseRecDaoList;
    }

    public List<CourseRecDao> listHot() {
        List<CourseRecDao> courseRecDaoList =  courseMapper.listHot();
        courseRecDaoList.forEach((CourseRecDao c) -> c.setRatings(gradeServices.getGradeDetail(c.getId())));
        return courseRecDaoList;
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
        for (Long key : mm.keySet()) {
            if (!courseIds.contains(key)) {
                m.put(key, mm.get(key).size());
            }
        }
        LinkedHashMap<Long, Integer> mmm = MyUtils.sortMap(m);
        Iterator<Map.Entry<Long, Integer>> it = mmm.entrySet().iterator();
        int i = 0;
        List<String> ids = new ArrayList<>();
        while (it.hasNext() && i < 6) {
            Map.Entry<Long, Integer> entry = it.next();
            ids.add(String.valueOf(entry.getKey()));
            i++;
        }
        if (CollectionUtils.isEmpty(ids)) return Collections.emptyList();
        List<CourseRecDao> courseRecDaoList = courseMapper.findByIds(ids);
        courseRecDaoList.forEach((CourseRecDao c) -> c.setRatings(gradeServices.getGradeDetail(c.getId())));
        return courseRecDaoList;
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
        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(6, similarity, dataModel);
        List<String> ids = Arrays.stream(userNeighborhood.getUserNeighborhood(courseId)).boxed().map(String::valueOf).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) return Collections.emptyList();
        List<CourseRecDao> courseRecDaoList = courseMapper.findByIds(ids);
        courseRecDaoList.forEach((CourseRecDao c) -> c.setRatings(gradeServices.getGradeDetail(c.getId())));
        return courseRecDaoList;
    }

    public List<CourseDaoWithGrade> getCard(Map<String, Object> map){
        List<CourseDao> courseDaoList;
        if(map.get("key")!=null){
            courseDaoList = courseMapper.findByKeyword((String)map.get("key"));
        }
        else{
            courseDaoList = courseMapper.listCourse();
        }
        if(map.get("filter") != null){
            Map<String, Object> filter = (Map<String, Object>) map.get("filter");
            if(filter.get("difficulty") != null){
                courseDaoList = courseDaoList.stream().filter(x -> filter.get("difficulty") == x.getDifficulty()).collect(Collectors.toList());
            }
            if(filter.get("subcategory_ids") != null){
                ArrayList<Integer> arrayList = (ArrayList<Integer>)filter.get("subcategory_ids");
                courseDaoList = courseDaoList.stream().filter(x -> arrayList.contains(x.getSubcategory_id().intValue())).collect(Collectors.toList());
            }
        }
        Boolean ascending = (Boolean) map.get("ascending");
        String sort = "";
        if(map.get("sort") != null){
            sort = (String) map.get("sort");
        }
        if(sort.equals("alphabet")){
            if(ascending){
                courseDaoList = courseDaoList.stream().sorted(Comparator.comparing(CourseDao::getName, Comparator.naturalOrder())).collect(Collectors.toList());
            }
            else{
                courseDaoList = courseDaoList.stream().sorted(Comparator.comparing(CourseDao::getName, Comparator.reverseOrder())).collect(Collectors.toList());
            }
        }
        if(sort.equals("time")){
            if(ascending){
                courseDaoList = courseDaoList.stream().sorted(Comparator.comparing(CourseDao::getId, Comparator.naturalOrder())).collect(Collectors.toList());
            }
            else{
                courseDaoList = courseDaoList.stream().sorted(Comparator.comparing(CourseDao::getId, Comparator.reverseOrder())).collect(Collectors.toList());
            }
        }
        if(sort.equals("difficulty")){
            if(ascending){
                courseDaoList = courseDaoList.stream().sorted(Comparator.comparing(CourseDao::getDifficulty, Comparator.naturalOrder())).collect(Collectors.toList());
            }
            else{
                courseDaoList = courseDaoList.stream().sorted(Comparator.comparing(CourseDao::getDifficulty, Comparator.reverseOrder())).collect(Collectors.toList());
            }
        }
        List<CourseDaoWithGrade> courseDaoWithGradeList = courseDaoList.stream().map(x -> {CourseDaoWithGrade courseDaoWithGrade = new CourseDaoWithGrade(x); courseDaoWithGrade.setRatings(gradeServices.getGradeDetail(x.getId())); return courseDaoWithGrade;}).collect(Collectors.toList());

        class RatingComparator implements Comparator<CourseDaoWithGrade> {
            @Override
            public int compare(CourseDaoWithGrade a, CourseDaoWithGrade b) {
                List<Long> listA = a.getRatings();
                double gradeA = (double)(listA.get(0) + listA.get(1)*2 + listA.get(2)*3 + listA.get(3)*4 + listA.get(4)*5)/(listA.get(0) + listA.get(1) + listA.get(2) + listA.get(3) + listA.get(4));
                List<Long> listB = b.getRatings();
                double gradeB = (double)(listB.get(0) + listB.get(1)*2 + listB.get(2)*3 + listB.get(3)*4 + listB.get(4)*5)/(listB.get(0) + listB.get(1) + listB.get(2) + listB.get(3) + listB.get(4));
                return (int) (gradeA - gradeB);
            }
        }

        if(sort.equals("rating")){
            if(ascending){
                Collections.sort(courseDaoWithGradeList, new RatingComparator());
            }
            else{
                Collections.sort(courseDaoWithGradeList, new RatingComparator());
                Collections.reverse(courseDaoWithGradeList);
            }
        }
        if(map.get("range")!=null){
            Integer from = ((Map<String, Integer>) map.get("range")).get("from");
            Integer size = ((Map<String, Integer>) map.get("range")).get("size");
            courseDaoWithGradeList = courseDaoWithGradeList.subList(from, from + size);
        }
        if(!(boolean) map.get("description")){
            courseDaoWithGradeList.forEach((CourseDaoWithGrade c) -> c.setDescription(""));
        }
        return courseDaoWithGradeList;
    }

    public Integer getNumber(){
        return courseMapper.getNumber();
    }
}
