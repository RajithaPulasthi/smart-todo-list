import React, { useState } from "react";
import AddButton from "./AddButton";
import TodoTaskPopup from "./TodoTaskPopup";
import TodoComponent from "./TodoComponent";

interface Task {
  taskName: string;
  location: string;
  priority: string;
}

const Background = () => {
  const [tasks, setTasks] = useState<Task[]>([]); // Persistent task state
  const [isPopupOpen, setIsPopupOpen] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);
  const [taskToEdit, setTaskToEdit] = useState<Task | null>(null);

  // Add a new task
  const handleAddTask = (taskData: Task) => {
    console.log("Adding Task:", taskData); // Debug
    setTasks((prevTasks) => [...prevTasks, taskData]);
    setIsPopupOpen(false); // Close popup
  };

  // Update an existing task
  const handleUpdateTask = (updatedTask: Task) => {
    console.log("Updating Task:", updatedTask); // Debug
    setTasks((prevTasks) =>
      prevTasks.map((task) =>
        task.taskName === taskToEdit?.taskName ? updatedTask : task
      )
    );
    setIsPopupOpen(false); // Close popup
    setIsEditMode(false);
    setTaskToEdit(null);
  };

  const handleEditClick = (task: Task) => {
    console.log("Editing Task:", task); // Debug
    setTaskToEdit(task);
    setIsEditMode(true);
    setIsPopupOpen(true);
  };

  const handleDeleteTask = (taskToDelete: Task) => {
    console.log("Deleting Task:", taskToDelete); // Debug
    setTasks((prevTasks) => prevTasks.filter((task) => task !== taskToDelete));
  };

  return (
    <div className="flex flex-col items-center bg-blue-700">
      {/* TodoTaskPopup */}
      <TodoTaskPopup
        open={isPopupOpen}
        onClose={() => setIsPopupOpen(false)}
        onSubmit={isEditMode ? handleUpdateTask : handleAddTask}
        taskToEdit={taskToEdit}
        isEditMode={isEditMode}
      />

      {/* Render Todo Components */}
      <div className="mt-4 w-full flex flex-col items-center h-[66vh] overflow-auto">
        {tasks.map((task, index) => (
          <TodoComponent
            key={index}
            taskName={task.taskName}
            location={task.location}
            priority={task.priority}
            isChecked={false}
            onCheckChange={(checked) =>
              console.log(`Task ${index} checked:`, checked)
            }
            onEditClick={() => handleEditClick(task)}
            onDeleteClick={() => handleDeleteTask(task)}
          />
        ))}
      </div>

      {/* Add Button */}
      <div className="flex flex-row-reverse w-full mr-16">
        <AddButton onClick={() => setIsPopupOpen(true)} />
      </div>
    </div>
  );
};

export default Background;
