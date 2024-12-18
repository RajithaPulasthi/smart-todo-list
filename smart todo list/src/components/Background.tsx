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
  const [tasks, setTasks] = useState<Task[]>([]);
  const [isPopupOpen, setIsPopupOpen] = useState(false);

  // Function to handle new task submission
  const handleAddTask = (taskData: Task) => {
    setTasks((prevTasks) => [...prevTasks, taskData]);
  };

  return (
    <div className="flex flex-col items-center   bg-blue-700">
      {/* TodoTaskPopup */}
      <TodoTaskPopup
        open={isPopupOpen}
        onClose={() => setIsPopupOpen(false)}
        onSubmit={(taskData) => {
          handleAddTask(taskData);
          setIsPopupOpen(false); // Close the popup after adding the task
        }}
      />

      {/* Render List of TodoComponents */}
      <div className="mt-4 w-full flex flex-col items-center h-[66vh]">
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
