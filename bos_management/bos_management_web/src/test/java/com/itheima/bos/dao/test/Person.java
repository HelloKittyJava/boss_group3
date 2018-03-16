package com.itheima.bos.dao.test;

/**
 * ClassName:Person <br/>
 * Function: <br/>
 * Date: 2018年3月16日 上午9:00:33 <br/>
 */
public class Person {

    private int age;
    private String name;

    public Person() {

    }

    public Person(int age, String name) {
       
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }
    
    public String getProvince() {
        return "广东";
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person [age=" + age + ", name=" + name + "]";
    }

}
