package bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用于远程调用时破解对象的传输，该类必须实现Serializable接口
 * 否则在调用过程中，会抛出NotSerializable异常
 * @author chenjing
 */
public class User implements Serializable {

    /**
     * 用户名
     */
    private String name;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 构造函数
     * @param name 用户名
     * @param password 用户密码
     */
    public User(String name,String password) {
        super();
        this.name = name;
        this.password = password;
    }

    public User() {
    }

    /**
     * idea自动生成
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(password, user.password);
    }

    /**
     * idea自动生成
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }

    /**
     * idea自动生成
     * @return
     */
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='*******"  + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
