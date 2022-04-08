package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Address address = new Address("city", "street", "10");

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setHomeAddress(address);
            em.persist(member1);

            Address newAddress = new Address("newCity", address.getStreet(), address.getZipcode());
            member1.setHomeAddress(newAddress);

//            Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());
//
//            Member member2 = new Member();
//            member2.setUsername("member2");
//            member2.setHomeAddress(copyAddress);
//            em.persist(member2);

//            member1.getHomeAddress().setCity("newCity");

            tx.commit();
        }catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();

    }

    private void orphanRemoval() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();

            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);
            em.persist(child1);
            em.persist(child2);

            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
//            em.remove(findParent);
            findParent.getChildList().remove(0);

//            em.persist(child1);
//            em.persist(child2);

            tx.commit();
        }catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }

    private void fetchType() {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

//            Member member1 = new Member();
//            member1.setUsername("member1");
//            member1.setTeam(team);
//            em.persist(member1);
//
//            em.flush();
//            em.clear();
//
//            Member m = em.find(Member.class, member1.getId());
//            System.out.println("m.getTeam().getClass() = " + m.getTeam().getClass()); // 지연로딩을 하게 되면 연관된 것을 프록시로 가져옴
//
//            System.out.println("=======================");
//            m.getTeam().getName(); // 프록시 초기화
//            System.out.println("=======================");

            tx.commit();
        }catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();

    }

    private void proxy() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member1 = new Member();
            member1.setUsername("member1");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            em.persist(member2);

            em.flush();
            em.clear();

            Member m1 = em.find(Member.class, member1.getId());
            System.out.println("m1.getClass() = " + m1.getClass());

            Member ref1 = em.getReference(Member.class, member1.getId());
            System.out.println("ref1.getClass() = " + ref1.getClass());

            System.out.println("(m1 == ref1) = " + (m1 == ref1));

            Member m2 = em.getReference(Member.class, member2.getId());

//            em.detach(m2);

            m2.getUsername(); // 프록시 객체 초기화
            System.out.println("m2.getUsername() = " + m2.getUsername());
            System.out.println("m2.getClass() = " + m2.getClass());

            System.out.println("(m1.getClass() == m2.getClass()) = " + (m1.getClass() == m2.getClass())); // true


//            Member findMember = em.find(Member.class, member.getId());
            Member findMember = em.getReference(Member.class, member1.getId()); // id 조회 시에는 프록시 객체 사용, username은 없으므로 db에 select
            System.out.println("findMember = " + findMember.getClass()); // findMember = class hellojpa.Member$HibernateProxy$PcYJTBkb
            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getUsername() = " + findMember.getUsername());

            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(findMember));

//            Member member = em.find(Member.class, 1L);
//
//            printMember(member); // member만 사용할건데 team을 한 번에 같이 가져올 필요가? -> 지연로딩으로 해결
////            printMemberAndTeam(member);

            tx.commit();
        }catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }

    private static void printMember(Member member) {
        System.out.println("member.getUsername() = " + member.getUsername());
    }

    private static void printMemberAndTeam(Member member) {
        String username = member.getUsername();
        System.out.println("username = " + username);

//        Team team = member.getTeam();
//        System.out.println("team.getName() = " + team.getName());
    }

    private void inheritanceExampleCode() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("user");
            member.setCreatedBy("kim");
            member.setCreatedDate(LocalDateTime.now());

            em.persist(member);

//            Movie movie = new Movie();
//            movie.setDirector("aaa");
//            movie.setActor("bbb");
//            movie.setName("바람과함께사라지다");
//            movie.setPrice(10000);
//            em.persist(movie);
//
//            em.flush();
//            em.clear();
//
////            Item item = em.find(Item.class, movie.getId());
////            System.out.println("item = " + item);
//
//            Movie findMovie = em.find(Movie.class, movie.getId());
//            System.out.println("findMovie = " + findMovie);

            tx.commit();
        }catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }

    private void previousExampleCode() {
        // 애플리케이션 로딩 시점에 딱 하나만 만들어야함, 애플리케이션 전체에서 공유
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 트랜잭션 단위마다 엔티티 매니저 만들어줘야함, 쓰레드간에 공유X, 사용하고 버려야함
        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("TeamA");
//            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
//            member.setTeam(team);
            em.persist(member);

//            team.getMembers().add(member);
//            em.persist(team);

            // flush, clear 하지 않으면 영속성 컨텍스트에 있는 entity를 가져오므로, select 쿼리 안 나감
//            em.flush();
//            em.clear();

            Member findMember = em.find(Member.class, member.getId());
//            List<Member> members = findMember.getTeam().getMembers();

//            for (Member m : members) {
//                System.out.println("m.getUsername() = " + m.getUsername());
//            }

            tx.commit();
            ////////////////////////////////////////////////////////////////////

//            Member memberA = new Member();
//            memberA.setUsername("A");
//            Member memberB = new Member();
//            memberB.setUsername("B");
//            Member memberC = new Member();
//            memberC.setUsername("C");
//
//            System.out.println("======================");
//            em.persist(memberA);
//            em.persist(memberB);
//            em.persist(memberC);
//            System.out.println("memberA.getId() = " + memberA.getId());
//            System.out.println("memberB.getId() = " + memberB.getId());
//            System.out.println("memberC.getId() = " + memberC.getId());
//            System.out.println("======================");
//            tx.commit();

            ////////////////////////////////////////////////////////////////////
//            Member member = em.find(Member.class, 150L);
//
//            em.detach(member); // 영속성 컨텍스트에서 빠짐 => 준영속
////             em.clear(); // 영속성 컨텍스트에 있는 것 전부 지움, 완전 초기화 => 준영속
////             em.close(); // 영속성 컨텍스트 종료 => 준영속
//            System.out.println("======================");
//
//            Member member2 = em.find(Member.class, 150L); // 1차 캐시 지웠기 떄문에 다시 조회
//
//            System.out.println("======================");
//            tx.commit();

            ////////////////////////////////////////////////////////////////////
//            Member member = new Member(200L, "member200");
//            em.persist(member);
//            em.flush(); // flush 할 때 DB에 쿼리 반영, 1차 캐시 유지, 영속성 컨텍스트의 쓰기지연 SQL 저장소에 쌓여있는 것들이 DB에 반영
//            System.out.println("======================");
//            tx.commit();

            ////////////////////////////////////////////////////////////////////
//            Member member = em.find(Member.class, 150L);
//            member.setName("ZZZZZ"); // 변경 감지 dirty check
//            // 1차캐시 안에 pk와 entity, 스냅샷이 있다(최초로 영속성 컨텍스트에 들어온 상태)
//            // tx.commit() 될때 내부적으로 flush 실행하면서 entity와 스냅샷을 비교 -> 변경이 있을 경우 쓰기 지연 SQL 저장소에 업데이트 쿼리 생성
//
//            System.out.println("======================");
//            tx.commit(); // 커밋할때 DB에 쿼리 나감

            ////////////////////////////////////////////////////////////////////
//            Member member1 = new Member(150L, "A");
//            Member member2 = new Member(160L, "B");
//
//            em.persist(member1);
//            em.persist(member2);
//
//            System.out.println("======================");
//            tx.commit(); // 커밋할때 DB에 쿼리 나감

            ////////////////////////////////////////////////////////////////////
//            Member findMember1 = em.find(Member.class, 101L);
//            Member findMember2 = em.find(Member.class, 101L); // 똑같은 걸로 두 번 조회하면 영속성 캐시에 있는걸 반환..
//
//            // == 비교 가능 동일성 보장, 1차캐시가 있기 떄문에 가능(REPEATABLE READ등급의 트랜잭션 격리 수준을 데이터베이스가 아닌 애플리케이션 차원에서 제공)
//            System.out.println("(findMember1 == findMember2) = " + (findMember1 == findMember2));

            ////////////////////////////////////////////////////////////////////
            // 비영속
//            Member member = new Member();
//            member.setId(101L);
//            member.setName("HelloJPA");
//
//            // 영속
//            System.out.println("=== BEFORE ===");
//            em.persist(member); // 저장
//            System.out.println("=== AFTER ===");
//
//            Member findMember = em.find(Member.class, 101L); // 조회할 때 조회쿼리 안나감..
//            System.out.println("findMember.getId() = " + findMember.getId());
//            System.out.println("findMember.getName() = " + findMember.getName());
//            findMember.setName("HelloJPA"); // 수정
//            em.remove(findMember); // 삭제

            ////////////////////////////////////////////////////////////////////
            // JPQL: 엔티티 객체를 대상으로 쿼리, SQL: 데이터베이스 테이블을 대상으로 쿼리
//            List<Member> result = em.createQuery("select m from Member as m", Member.class)
//                    // 페이징(방언에 맞춰서 DB에 맞게 번역)
//                    .setFirstResult(1)
//                    .setMaxResults(10)
//                    .getResultList();
//            for (Member member : result) {
//                System.out.println("member.getName() = " + member.getName());
//            }

//            tx.commit();
        }catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();
    }
}
