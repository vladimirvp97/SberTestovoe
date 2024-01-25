package com.example.service;

import com.example.controller.request.CreateTaskRequest;
import com.example.dao.TaskRepository;
import com.example.model.Priority;
import com.example.model.Status;
import com.example.model.Task;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public ResponseEntity<Task> createTask(CreateTaskRequest createTaskRequest){
        Task task = Task.builder()
                .heading(createTaskRequest.getHeading())
                .description(createTaskRequest.getDescription())
                .status(createTaskRequest.getStatus())
                .priority(createTaskRequest.getPriority())
                .executorName(createTaskRequest.getExecutorName()).build();
        Task savedTask = taskRepository.save(task);

        return new ResponseEntity<Task>(savedTask ,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Task> editTask(Long taskId, CreateTaskRequest ctr){
        if (!taskRepository.existsById(taskId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Task task = new Task(taskId, ctr.getHeading(), ctr.getDescription(), ctr.getStatus(),
                ctr.getPriority(), ctr.getExecutorName());
        Task editedTask = taskRepository.save(task);

        return new ResponseEntity<Task>(editedTask, HttpStatus.OK);
    }

    public ResponseEntity<Task> findTaskById(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Task task = optionalTask.get();
        return new ResponseEntity<Task>(task, HttpStatus.OK);
    }

    public Page<Task> findTasks(String executorName,
                                Status status,
                                Priority priority,
                                Pageable pageable){
        if (executorName == null && status == null && priority == null) {
            return taskRepository.findAll(pageable);
        }

        Task task = Task.builder()
                .executorName(executorName)
                .priority(priority)
                .status(status).build();

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<Task> example = Example.of(task, matcher);

        return taskRepository.findAll(example, pageable);
    }

    @Transactional
    public ResponseEntity<Void> deleteTask(Long taskId){
        if (!taskRepository.existsById(taskId))
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        taskRepository.deleteById(taskId);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
