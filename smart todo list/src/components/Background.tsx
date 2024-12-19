import React, { useState } from "react";
import AddButton from "./AddButton";
import TodoTaskPopup from "./TodoTaskPopup";
import TodoComponent from "./TodoComponent";

interface Task {
  taskName: string;
  location: string;
  priority: string;
  completed: boolean;
}

const Background = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [isPopupOpen, setIsPopupOpen] = useState(false);
  const [taskToEdit, setTaskToEdit] = useState<Task | null>(null);
  const [isEditMode, setIsEditMode] = useState(false);

  // Function to handle new task submission
  const handleAddTask = (taskData: Task) => {
    setTasks((prevTasks) => [...prevTasks, { ...taskData, completed: false }]);
  };

  // Function to handle task update
  const handleUpdateTask = (updatedTask: Task) => {
    setTasks((prevTasks) =>
      prevTasks.map((task) =>
        task === taskToEdit ? { ...task, ...updatedTask } : task
      )
    );
    setTaskToEdit(null);
    setIsEditMode(false);
  };

  const handleCheckChange = (index: number, checked: boolean) => {
    setTasks((prevTasks) => {
      const updatedTasks = [...prevTasks];
      updatedTasks[index].completed = checked;
      updatedTasks.sort((a, b) => Number(a.completed) - Number(b.completed)); // Sort by completed status
      return updatedTasks;
    });
  };

  const handleEditClick = (task: Task) => {
    setTaskToEdit(task);
    setIsEditMode(true);
    setIsPopupOpen(true);
  };

  const handleDeleteTask = (index: number) => {
    setTasks((prevTasks) => prevTasks.filter((_, i) => i !== index));
  };

  return (
    <div className="flex flex-col items-center bg-blue-700">
      <TodoTaskPopup
        open={isPopupOpen}
        onClose={() => {
          setIsPopupOpen(false);
          setTaskToEdit(null);
          setIsEditMode(false);
        }}
        onSubmit={isEditMode ? handleUpdateTask : handleAddTask}
        taskToEdit={taskToEdit}
        isEditMode={isEditMode}
      />

      <div className="mt-4 w-full flex flex-col items-center h-[66vh]">
        {tasks.map((task, index) => (
          <TodoComponent
            key={index}
            taskName={task.taskName}
            location={task.location}
            priority={task.priority}
            isChecked={task.completed}
            onCheckChange={(checked) => handleCheckChange(index, checked)}
            onEditClick={() => handleEditClick(task)}
            onDeleteClick={() => handleDeleteTask(index)}
          />
        ))}
      </div>
      <div className="flex flex-row-reverse w-full mr-16">
        <AddButton onClick={() => setIsPopupOpen(true)} />
      </div>
    </div>
  );
};

export default Background;
