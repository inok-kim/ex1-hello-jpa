package hellojpa;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 단일 테이블 전략에서는 @DiscriminatorColumn 어노테이션이 없어도 DType 컬럼이 생성됨
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // 추상 클래스로 만들어야함. 일반 클래스일 경우 Item 테이블 만들어짐, item클래스로 조회할 경우 union all로 조회해옴...
@DiscriminatorColumn
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
