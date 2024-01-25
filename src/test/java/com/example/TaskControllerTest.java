package com.example;

import com.example.dao.TaskRepository;
import com.example.model.Priority;
import com.example.model.Status;
import com.example.model.Task;
import com.example.controller.request.CreateTaskRequest;
import com.example.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskRepository taskRepository;

	@Test
	public void testGetTasksWithoutParams() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetTasksWithStatusParam() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/tasks")
						.param("status", "AT_WORK"))
				.andExpect(status().isOk());
	}

	@Test
	public void testCreateTask() throws Exception {
		CreateTaskRequest newTask = new CreateTaskRequest("Header", "Description", Status.AT_WORK, Priority.HIGH, "name");
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(newTask);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andReturn();

		String responseString = result.getResponse().getContentAsString();

		Task responseTask = objectMapper.readValue(responseString, Task.class);

		assertNotNull(responseTask);
		assertEquals("Header", responseTask.getHeading());
		assertEquals("Description", responseTask.getDescription());
		assertEquals(Status.AT_WORK, responseTask.getStatus());
		assertEquals(Priority.HIGH, responseTask.getPriority());
		assertEquals("name", responseTask.getExecutorName());
	}


	@Test
	public void testEditTask() throws Exception {
		Task taskSave = createTask();

		taskRepository.save(taskSave);

		Optional<Task> optionalTask = taskRepository.findById(1L);
		Task taskChanged = optionalTask.get();
		taskChanged.setDescription("Change for test");

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(taskChanged);

		mockMvc.perform(MockMvcRequestBuilders.put("/tasks/{taskId}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk());

		Optional<Task> optionalResponseTask = taskRepository.findById(1L);
		Task responseTask = optionalResponseTask.get();

		assertEquals(responseTask.getDescription(), taskChanged.getDescription());
	}

	@Test
	public void testDeleteTask() throws Exception {
		Task task = createTask();
		taskRepository.save(task);

		mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/{taskId}", task.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk());

		boolean taskExists = taskRepository.existsById(task.getId());
		assertFalse(taskExists);
	}

	public Task createTask() {
		Task taskSave = new Task(1L, "Heading", "Description",
				Status.AT_WORK, Priority.HIGH, "Executor");
		return taskSave;
	}
}
