package pub.iseekframework.utils.collection;

import java.lang.reflect.Field;
import java.util.*;

import org.apache.commons.collections4.Equator;

/**
 * desc：     集合公共类 <br>
 * author：   wangsong <br>
 * date：     2018/5/14 下午7:04 <br>
 * version：  v1.0.0 <br>
 */
public class CollectionUtils {

    /**
     * 将集合中的成员按照某个字段映射并生成相应的ArrayList
     * @param collection 提取对象
     * @param fieldName  字段名称
     * @param <T>        成员类型
     * @param <E>        字段类型
     * @return           list结果集
     */
    public static <T,E> List<E> takeFieldsToList(Collection<T> collection, String fieldName) {
        if (collection == null) {
            return new ArrayList<>();
        }
        List<E> result = new ArrayList<>(collection.size());
        putElement(result,collection,fieldName);
        return result;
    }

    /**
     * 将集合中的成员按照某个字段映射并生成相应的HashSet
     * @param collection 提取对象
     * @param fieldName  字段名称
     * @param <T>        成员类型
     * @param <E>        字段类型
     * @return           set结果集
     */
    public static <T,E> Set<E> takeFieldsToSet(Collection<T> collection, String fieldName) {
        if (collection == null) {
            return new HashSet<>();
        }
        Set<E> result = new HashSet<>(collection.size());
        putElement(result,collection,fieldName);
        return result;
    }

    /**
     * 将集合中的成员按照某个字段映射并生成相应的HashMap
     * @param collection 归类对象
     * @param fieldName  字段名称
     * @param <K>        字段类别
     * @param <T>        成员类型
     * @return           map结果集
     */
    @SuppressWarnings("unchecked")
    public static <K,T> Map<K,T> takeFieldsToMap(Collection<T> collection, String fieldName) {
        if (collection == null) {
            return new HashMap<>(1);
        }
        Map<K,T> map = new HashMap<>(collection.size());
        try {
            for (T t : collection) {
                Field field = t.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                K key = (K)field.get(t);
                map.put(key,t);
            }
        } catch (Exception e) {
            throw new RuntimeException("invalid field or fieldType: " + fieldName);
        }

        return map;
    }

    /**
     * 将集合中的成员按照某个字段归类并生成相应的map
     * @param collection 归类对象
     * @param fieldName  字段名称
     * @param <K>        字段类别
     * @param <T>        成员类型
     * @return           map结果集
     */
    @SuppressWarnings("unchecked")
    public static <K,T> Map<K,List<T>> classifyByFieldToMap(Collection<T> collection, String fieldName) {
        if (collection == null) {
            return new HashMap<>(1);
        }
        Map<K,List<T>> map = new HashMap<>(collection.size());
        try {
            for (T t : collection) {
                Field field = t.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                K key = (K)field.get(t);
                List<T> tList = map.get(key);
                if (tList == null) {
                    tList = new ArrayList<>(16);
                    map.put(key,tList);
                }
                tList.add(t);
            }
        } catch (Exception e) {
            throw new RuntimeException("invalid field or fieldType: " + fieldName);
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    private static <T,E> void putElement(
            Collection<E> collection,
            Collection<T> queryCollection,
            String fieldName) {
        try {
            for (T t : queryCollection) {
                Field field = t.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object o = field.get(t);
                collection.add((E) o);
            }
        } catch (Exception e) {
            throw new RuntimeException("invalid field or fieldType: " + fieldName);
        }
    }


    /**
     * Description：判断集合不为空 <br>
     * Author： wangsong <br>
     * params： [coll] <br>
     * Date： 2018/5/21 下午5:46 <br>
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return org.apache.commons.collections4.CollectionUtils.isNotEmpty(coll);
    }

    /**
     * Description：判断集合为空 <br>
     * Author： wangsong <br>
     * params： [coll] <br>
     * Date： 2018/5/21 下午5:47 <br>
     */
    public static boolean isEmpty(Collection<?> coll) {
        return org.apache.commons.collections4.CollectionUtils.isEmpty(coll);
    }

    /**
     * Description：比较两个集合是否相等 <br>
     * Author： wangsong <br>
     * params： [a, b] <br>
     * Date： 2018/5/21 下午5:51 <br>
     */
    public static boolean isEqualCollection(Collection<?> a, Collection<?> b) {
        return org.apache.commons.collections4.CollectionUtils.
                isEqualCollection(a, b);
    }


    /**
     * Description：比较两个集合是否相等 - 自定义Equator，参考网址：http://www.jb51.net/article/113712.htm <br>
     * Author： wangsong <br>
     * params： [a, b, equator] <br>
     * Date： 2018/5/21 下午5:54 <br>
     */
    public static <E> boolean isEqualCollection(Collection<? extends E> a,
                                                Collection<? extends E> b,
                                                final Equator<? super E> equator) {
        return org.apache.commons.collections4.CollectionUtils.
                isEqualCollection(a, b, equator);
    }


    /**
     * Description：取两个集合的并集 <br>
     * Author： wangsong <br>
     * params： [a, b] <br>
     * Date： 2018/5/21 下午5:59 <br>
     */
    public static <O> Collection<O> union(Iterable<? extends O> a, Iterable<? extends O> b) {
        return org.apache.commons.collections4.CollectionUtils.union(a, b);
    }

    /**
     * Description：取两个集合的交集 <br>
     * Author： wangsong <br>
     * params： [a, b] <br>
     * Date： 2018/5/21 下午6:01 <br>
     */
    public static <O> Collection<O> intersection(Iterable<? extends O> a, Iterable<? extends O> b) {
        return org.apache.commons.collections4.CollectionUtils.intersection(a, b);
    }

    /**
     * Description：取两个集合的交集的补集 <br>
     * Author： wangsong <br>
     * params： [a, b] <br>
     * Date： 2018/5/21 下午6:03 <br>
     */
    public static <O> Collection<O> disjunction(Iterable<? extends O> a, Iterable<? extends O> b) {
        return org.apache.commons.collections4.CollectionUtils.disjunction(a, b);
    }

    /**
     * Description：取两个集合的差 <br>
     * Author： wangsong <br>
     * params： [a, b] <br>
     * Date： 2018/5/21 下午6:05 <br>
     */
    public static <O> Collection<O> subtract(Iterable<? extends O> a, Iterable<? extends O> b) {
        return org.apache.commons.collections4.CollectionUtils.subtract(a, b);
    }

    /**
     * Description：集合转List <br>
     * Author： wangsong <br>
     * params： [coll] <br>
     * Date： 2018/5/21 下午6:13 <br>
     */
    public static <V> List<V> asList(final java.util.Collection<V> coll) {
        if (CollectionUtils.isEmpty(coll)) {
            return new ArrayList<V>(0);
        }
        final List<V> list = new ArrayList<V>();
        for (final V value : coll) {
            if (value != null) {
                list.add(value);
            }
        }
        return list;
    }


    /**
     * 集合转String</p>
     * @param collection 泛型必须为String类型
     * @param split 比如连接符","
     * @return
     * @autho wangsong
     * @time 2017-4-10 下午3:22:24
     */
    public static String collToString(Collection<String> collection, String split) {
        StringBuilder sb = new StringBuilder();
        if (collection != null) {
            int i = 0, size = collection.size();
            for (Iterator<String> iterator = collection.iterator(); iterator.hasNext();) {
                String str = iterator.next();
                sb.append(str);
                if (++i < size) {
                    sb.append(split);
                }
            }
        }
        return sb.toString();
    }


    /**
     * Description：测试 <br>
     * Author： wangsong <br>
     * params： [args] <br>
     * Date： 2018/5/21 下午6:05 <br>
     */
    public static void main(String[] args) {
        List<Integer> a = new ArrayList<Integer>();
        List<Integer> b = null;
        List<Integer> c = new ArrayList<Integer>();
        c.add(5);
        c.add(6);
        //判断集合是否为空
        System.out.println(CollectionUtils.isEmpty(a));   //true
        System.out.println(CollectionUtils.isEmpty(b));   //true
        System.out.println(CollectionUtils.isEmpty(c));   //false

        //判断集合是否不为空
        System.out.println(CollectionUtils.isNotEmpty(a));   //false
        System.out.println(CollectionUtils.isNotEmpty(b));   //false
        System.out.println(CollectionUtils.isNotEmpty(c));   //true

        //两个集合间的操作
        List<Integer> e = new ArrayList<Integer>();
        e.add(2);
        e.add(1);
        List<Integer> f = new ArrayList<Integer>();
        f.add(1);
        f.add(2);
        List<Integer> g = new ArrayList<Integer>();
        g.add(12);
        //比较两集合值
        System.out.println(CollectionUtils.isEqualCollection(e,f));   //true
        System.out.println(CollectionUtils.isEqualCollection(f,g));   //false

        List<Integer> h = new ArrayList<Integer>();
        h.add(1);
        h.add(2);
        h.add(3);;
        List<Integer> i = new ArrayList<Integer>();
        i.add(3);
        i.add(3);
        i.add(4);
        i.add(5);
        //并集
        System.out.println(CollectionUtils.union(i,h));  //[1, 2, 3, 3, 4, 5]
        //交集
        System.out.println(CollectionUtils.intersection(i,h)); //[3]
        //交集的补集
        System.out.println(CollectionUtils.disjunction(i,h)); //[1, 2, 3, 4, 5]
        //e与h的差
        System.out.println(CollectionUtils.subtract(h,i)); //[1, 2]
        System.out.println(CollectionUtils.subtract(i,h)); //[3, 4, 5]

        //建立一个集合
        Map<String, String> j = new HashMap<String, String>();
        j.put("name", "ws");
        j.put("pwd", "123123");
        j.put("age", "21");

        System.out.println("集合转List: " + CollectionUtils.asList(j.values()));
        System.out.println("集合转String : " + CollectionUtils.collToString(j.values(), ","));

    }
}
