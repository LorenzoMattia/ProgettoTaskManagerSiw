package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
	
	public List<User> findByVisibleProjects(Project project);
	
	//public List<User> findByVisibleProjectsNotIn(Project project);
	
	@Query(	value = "	SELECT * "
					+ "	FROM users u "
					+ "	WHERE u.id NOT IN "
					+ "		(SELECT u.id "
					+ "		FROM users u, project_members pm "
					+ "		WHERE pm.members_id=u.id AND pm.visible_projects_id=:projectId) ",
			nativeQuery = true
	)
	public List<User> findByNotVisibleProjects(@Param("projectId") Long projectId);

}
