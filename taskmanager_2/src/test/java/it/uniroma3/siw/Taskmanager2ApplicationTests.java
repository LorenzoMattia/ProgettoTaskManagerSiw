package it.uniroma3.siw;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ProjectRepository;
import it.uniroma3.siw.repository.TaskRepository;
import it.uniroma3.siw.repository.UserRepository;
import it.uniroma3.siw.services.ProjectService;
//import it.uniroma3.siw.services.TaskService;
import it.uniroma3.siw.services.UserService;

@SpringBootTest
@RunWith(SpringRunner.class)
class Taskmanager2ApplicationTests {
	
	@Autowired
	private UserRepository ur;
	@Autowired
	private TaskRepository tr;
	@Autowired
	private ProjectRepository pr;
	@Autowired
	private UserService us;
	@Autowired
	private ProjectService ps;
	/*@Autowired
	private TaskService ts;*/
	
	@Before
	public void deleteAll() {
		System.out.println("Deleting all data..\n");
		this.ur.deleteAll();
		this.pr.deleteAll();
		this.tr.deleteAll();
		System.out.println("Done");
		}
	
	@Test
	void testUpdateUser() {
		User user1 = new User("mariorossi", "password", "mario", "rossi");
		user1 = us.saveUser(user1);
		assertEquals(user1.getId().longValue(), 1L);
		assertEquals(user1.getUsername(), "mariorossi");
		
		User user2 = new User("lucabianchi", "password2", "luca", "bianchi");
		user2 = us.saveUser(user2);
		assertEquals(user2.getId().longValue(), 2L);
		assertEquals(user2.getUsername(), "lucabianchi");
		
		User user1Update = new User("mariarossi", "password", "maria", "rossi");
		user1Update.setId(user1.getId());
		user1Update = us.saveUser(user1Update);
		assertEquals(user1Update.getId().longValue(), 1L);
		assertEquals(user1Update.getUsername(), "mariarossi");
		
		Project project1 = new Project("test project 1", "è il test project 1");
		project1.setOwner(user1Update);
		project1 = ps.saveProject(project1);
		assertEquals(project1.getOwner(), user1Update);
		assertEquals(project1.getName(), "test project 1");
		
		Project project2 = new Project("test project 2", "è il test project 2");
		project2.setOwner(user1Update);
		project2 = ps.saveProject(project2);
		assertEquals(project2.getOwner(), user1Update);
		assertEquals(project2.getName(), "test project 2");
		
		project1 = 	ps.shareProjectWithUser(project1, user2);
		List<Project> projects = pr.findByOwner(user1Update);
		assertEquals(projects.size(), 2);
		assertEquals(projects.get(0), project1);
		assertEquals(projects.get(1), project2);
		
		List<Project> projectsVisibleByUser2 = pr.findByMembers(user2);
		assertEquals(projectsVisibleByUser2.size(), 1);
		assertEquals(projectsVisibleByUser2.get(0), project1);
		
		List<User> project1Members = ur.findByVisibleProjects(project1);
		assertEquals(project1Members.size(), 1);
		assertEquals(project1Members.get(0), user2);
		
	}

}
