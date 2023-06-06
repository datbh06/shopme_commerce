package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userDatBH = new User("dat.bui@hcl.com", "dat1606", "Dat", "Bui Huu");
		userDatBH.addRole(roleAdmin);

		User savedUser = userRepo.save(userDatBH);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userRavi = new User("ravi@gmail.com", "ravi2022", "Ravi", "Kumar");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);

		userRavi.addRole(roleAssistant);
		userRavi.addRole(roleEditor);

		User savedUser = userRepo.save(userRavi);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUser() {
		Iterable<User> listUsers = userRepo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}

	@Test
	public void testGetUserById() {
		User userDat = userRepo.findById(1).get();
		System.out.println(userDat);
		assertThat(userDat).isNotNull();
	}

	@Test
	public void testUpdateUserDetails() {
		User userDat = userRepo.findById(13).get();
		userDat.setEnabled(true);
		userDat.setEmail("dat.bui@shopme.com");
		userRepo.save(userDat);
	}

	@Test
	public void testUpdateUserRoles() {
		User userRavi = userRepo.findById(8).get();
		Role roleEditor = new Role(3);
		userRavi.getRoles().remove(roleEditor);
		userRepo.save(userRavi);
	}

	@Test
	public void testDeleteUser() {
		Integer userid = 2;
		userRepo.deleteById(userid);
	}

	@Test
	public void testGetUserByEmail() {
		String email = "ravi@gmail.com";
		User user = userRepo.getUserByEmail(email);

		assertThat(user).isNotNull();
	}

	@Test
	public void testCountById() {
		Integer id = 24;
		Long countById = userRepo.countById(id);

		assertThat(countById).isNotNull().isGreaterThan(0);
	}

	@Test
	public void testDisabledUser() {
		Integer id = 13;
		userRepo.updateEnabledStatus(id, false);
	}

	@Test
	public void testEnabledUser() {
		Integer id = 13;
		userRepo.updateEnabledStatus(id, true);
	}

	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepo.findAll(pageable);

		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isEqualTo(pageSize);

	}

	@Test
	public void testSearchUsers() {
		String keyword = "bruce";

		int pageNumber = 0;
		int pageSize = 4;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepo.findAll(keyword, pageable);

		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isGreaterThan(0);
	}

}
