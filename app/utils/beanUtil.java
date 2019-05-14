package utils;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;
import models.Subscriber;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class beanUtil {

    public static void save(Object bean) {
        Ebean.save(bean);
    }

    public static void save(List beans) {
        Ebean.save(beans);
    }

    public static void update(Object bean) {
        Ebean.update(bean);
    }

    public static void update(List beans) {
        Ebean.update(beans);
    }

    public static void refresh(Object bean) {
        Ebean.refresh(bean);
    }

    public static void refresh(List beans) {
        Ebean.refresh(beans);
    }

    public static List getAll(Class type) {
        return Ebean.find(type).orderBy("id desc").findList();
    }

    public static List getSome(Class type, int rows) {
        return Ebean.find(type).setMaxRows(rows).orderBy("id desc").findList();
    }

    public static List getSomeWithProperty(Class type, String propertyName, Object value, int rows) {
        return Ebean.find(type).where().eq(propertyName, value).setMaxRows(rows).orderBy("id desc").findList();
    }

    public static List getAllWithProperty(Class type, String propertyName, Object value) {
        return Ebean.find(type).where().eq(propertyName, value).orderBy("id desc").findList();
    }

    public static List getAllWithPropertyIn(Class type, String propertyName, Object inValues) {
        return Ebean.find(type).where().in(propertyName, inValues).orderBy("id desc").findList();
    }

    public static List getAllWithProperties(Class type, Map properties) {
        return Ebean.find(type).where().allEq(properties).findList();
    }

    public static void insertSqlQuery(String sql){
        SqlUpdate insert = Ebean.createSqlUpdate(sql);
        insert.execute();
    }

    public static void newSubscriber(String msisdn){
        List subscribers = getAllWithProperty(Subscriber.class, "msisdn", msisdn);
        if(subscribers.isEmpty()){
            Subscriber subscriber = new Subscriber();
            subscriber.msisdn = msisdn;
            subscriber.insertDate = new Date();
            save(subscriber);
        }
    }

}
