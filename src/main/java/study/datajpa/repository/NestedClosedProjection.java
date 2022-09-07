package study.datajpa.repository;

//중첩 구조 관리
public interface NestedClosedProjection {

    String getUsername();
    TeamInfo getTeam();

    interface  TeamInfo{
        String getName();
    }
}
