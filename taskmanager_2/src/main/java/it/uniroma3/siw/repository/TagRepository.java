package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.Tag;
import it.uniroma3.siw.model.Task;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

	@Query(	value = "	SELECT * "
			+ "	FROM tag t "
			+ "	WHERE project_id=:projectId AND t.id NOT IN "
			+ "		(SELECT t.id "
			+ "		FROM tag t, task_tags tt "
			+ "		WHERE tt.tags_id=t.id AND tt.tasks_id=:taskId) ",
			nativeQuery = true
	)
	List<Tag> getTagsNotAddedById(@Param(value = "taskId") Long taskId, @Param(value = "projectId") Long projectId);

	List<Tag> findByTasksAndTasksProject(Task task, Project project);
	
	
}
