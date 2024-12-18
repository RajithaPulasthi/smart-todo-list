import React, { useState, useEffect } from "react";
import CancelOutlinedIcon from "@mui/icons-material/CancelOutlined";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import Button from "@mui/material/Button";

interface TodoTaskPopupProps {
  open: boolean;
  onClose: () => void;
  onSubmit: (taskData: { taskName: string; location: string; priority: string }) => void;
  taskToEdit?: { taskName: string; location: string; priority: string } | null;
  isEditMode: boolean;
}

const options = ["Home", "Work", "Gym", "Supermarket"];
const priorityOptions = ["A", "B", "C"];

const TodoTaskPopup = ({
  open,
  onClose,
  onSubmit,
  taskToEdit,
  isEditMode,
}: TodoTaskPopupProps) => {
  const [taskName, setTaskName] = useState("");
  const [location, setLocation] = useState<string | null>(options[0]);
  const [priority, setPriority] = useState(priorityOptions[0]);

  // Populate fields when editing
  useEffect(() => {
    if (isEditMode && taskToEdit) {
      setTaskName(taskToEdit.taskName);
      setLocation(taskToEdit.location);
      setPriority(taskToEdit.priority);
    }
  }, [isEditMode, taskToEdit]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit({ taskName, location: location || "", priority });
  };

  return (
    <div
      className={`fixed inset-0 z-50 overflow-y-auto 
      bg-black/50 backdrop-blur-sm 
      flex items-center justify-center 
      p-4 sm:p-6 md:p-8 ${open ? "visible" : "invisible"}`}
    >
      <div className="bg-blue-700 rounded-2xl">
        <div className="flex justify-end p-4">
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600">
            <CancelOutlinedIcon />
          </button>
        </div>
        <div className="p-4 w-[30vw] h-[50vh]">
          <form onSubmit={handleSubmit}>
            <div className="sm:col-span-3">
              <label className="block font-medium text-white text-xl">
                Task Name
              </label>
              <TextField
                id="task-name"
                label="Task"
                variant="outlined"
                value={taskName}
                onChange={(e) => setTaskName(e.target.value)}
                required
                fullWidth
              />
            </div>
            <div className="sm:col-span-3 mt-4">
              <label className="block font-medium text-white text-xl">
                Location
              </label>
              <Autocomplete
                value={location}
                onChange={(_, newValue: string | null) => setLocation(newValue)}
                options={options}
                renderInput={(params) => (
                  <TextField {...params} label="Select Location" required />
                )}
                fullWidth
              />
            </div>
            <div className="sm:col-span-3 mt-4">
              <label className="block font-medium text-white text-xl">
                Priority
              </label>
              <Autocomplete
                value={priority}
                onChange={(_, newValue: string | null) =>
                  setPriority(newValue || priorityOptions[0])
                }
                options={priorityOptions}
                renderInput={(params) => (
                  <TextField {...params} label="Select Priority" required />
                )}
                fullWidth
              />
            </div>
            <div className="mt-6">
              <Button type="submit" variant="contained" color="primary">
                OK
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default TodoTaskPopup;
