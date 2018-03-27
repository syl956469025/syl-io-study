package syl.study.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 序列化 反序列化工具
 */
public class FastJsonUtil {

    private FastJsonUtil(){}

    /**
     * 对象序列化为字符串
     * @param obj
     * @return
     */
    public static String bean2Json(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * 含有日期的对象序列化为字符串
     * @param obj
     * @param dateFormat
     * @return
     */
    public static String bean2Json(Object obj, String dateFormat) {
        return JSON.toJSONStringWithDateFormat(obj, dateFormat, SerializerFeature.PrettyFormat);
    }

    /**
     * 字符串反序列化为指定对象
     * @param jsonStr
     * @param objClass
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String jsonStr, Class<T> objClass) {
        return JSON.parseObject(jsonStr, objClass);
    }

    /**
     * 对象转换为Map
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> bean2Map(Object obj) {
        return  json2Bean(JSON.toJSONString(obj), HashMap.class);
    }



    /**
     * map 转换为指定对象
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T>T map2Bean(Map<String,Object> map,Class<T> clazz){
        return json2Bean(bean2Json(map), clazz);
    }

    /**
     * 字符串转换为指定对象列表
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> json2Array(String json,Class<T> clazz){
        return JSON.parseArray(json, clazz);
    }


    
    
    
}
