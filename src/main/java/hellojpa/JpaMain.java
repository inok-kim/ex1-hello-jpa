package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        // 애플리케이션 로딩 시점에 딱 하나만 만들어야함, 애플리케이션 전체에서 공유
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 트랜잭션 단위마다 엔티티 매니저 만들어줘야함, 쓰레드간에 공유X, 사용하고 버려야함
        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
//            Member member = new Member();
//            member.setId(1L);
//            member.setName("HelloB");
//            em.persist(member); // 저장

//            Member findMember = em.find(Member.class, 1L);
//            findMember.setName("HelloJPA"); // 수정
//            em.remove(findMember); // 삭제

            // JPQL: 엔티티 객체를 대상으로 쿼리, SQL: 데이터베이스 테이블을 대상으로 쿼리
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    // 페이징(방언에 맞춰서 DB에 맞게 번역)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();
            for (Member member : result) {
                System.out.println("member.getName() = " + member.getName());
            }

            tx.commit();
        }catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }


        emf.close();

    }
}
