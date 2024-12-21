import CancelOutlinedIcon from "@mui/icons-material/CancelOutlined";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import React, { useState, useEffect } from "react";

const LocationAPI = "http://localhost:8094/journey-genie-backend-api/locations";

interface TodoTaskPopupProps {
  open: boolean;
  onClose: () => void;
  onSubmit: (taskData: {
    taskName: string;
    location: string;
    priority: number;
    completed: boolean;
  }) => void;
  taskToEdit?: {
    taskName: string;
    location: string;
    priority: number;
    completed: boolean;
  } | null;
  isEditMode: boolean;
}

interface LocationDataProps {
  id: number;
  name: string;
  latitude: number;
  longitude: number;
}

const priorityOptions = [1, 2, 3];

const TodoTaskPopup = ({
  open,
  onClose,
  onSubmit,
  taskToEdit,
  isEditMode,
}: TodoTaskPopupProps) => {
  const [locationData, setLocationData] = useState<LocationDataProps[]>([]);
  const [taskName, setTaskName] = useState("");
  const [selectedLocation, setSelectedLocation] = useState<string>("");
  const [priority, setPriority] = useState(priorityOptions[0]);

  // Fetch locations when popup opens
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(LocationAPI);
        const data = (await response.json()) as LocationDataProps[];
        setLocationData(data);
      } catch (error) {
        console.error("Error fetching locations:", error);
      }
    };

    if (open) {
      fetchData();
    }
  }, [open]);

  // Set form values when editing
  useEffect(() => {
    if (isEditMode && taskToEdit) {
      setTaskName(taskToEdit.taskName);
      setSelectedLocation(taskToEdit.location);
      setPriority(taskToEdit.priority);
    } else {
      setTaskName("");
      setSelectedLocation("");
      setPriority(priorityOptions[0]);
    }
  }, [isEditMode, taskToEdit, open]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit({
      taskName,
      location: selectedLocation,
      priority,
      completed: taskToEdit?.completed || false,
    });
    // Reset form
    setTaskName("");
    setSelectedLocation("");
    setPriority(priorityOptions[0]);
    onClose();
  };

  return (
    <div
      className={`fixed inset-0 z-50 overflow-y-auto 
        bg-black/50 backdrop-blur-sm 
        flex items-center justify-center 
        p-4 ${open ? "visible" : "invisible"}`}
    >
      <div className="bg-blue-700 rounded-2xl">
        <div className="flex justify-end p-4">
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600"
          >
            <CancelOutlinedIcon />
          </button>
        </div>
        <div className="p-4 w-[25vw] h-[50vh] flex flex-col items-center justify-center">
          <form onSubmit={handleSubmit}>
            <div className="sm:col-span-3 justify-center flex flex-col">
              <label className="block font-medium text-white text-xl">
                What is your task?
              </label>
              <Box sx={{ "& > :not(style)": { m: 1, width: "25ch" } }}>
                <TextField
                  id="task-name"
                  label="Task"
                  variant="outlined"
                  value={taskName}
                  onChange={(e) => setTaskName(e.target.value)}
                  required
                />
              </Box>
            </div>

            <div className="sm:col-span-3 mt-2">
              <label className="block font-medium text-white text-xl">
                Task Location?
              </label>
              <Autocomplete
                value={selectedLocation}
                onChange={(_, newValue) => setSelectedLocation(newValue || "")}
                options={locationData.map((option) => option.name)}
                sx={{ width: 300 }}
                renderInput={(params) => (
                  <TextField {...params} label="Select Location" required />
                )}
              />
            </div>

            <div className="sm:col-span-3 mt-2">
              <label className="block mb-1 font-medium text-white text-xl">
                Priority
              </label>
              <Autocomplete
                value={priority}
                onChange={(_, newValue) => setPriority(newValue || priorityOptions[0])}
                id="task-priority"
                options={priorityOptions}
                renderInput={(params) => (
                  <TextField {...params} label="Select Priority" required />
                )}
                sx={{ width: 300 }}
              />
            </div>

            <div className="mt-2 flex justify-end">
              <Button type="submit" variant="contained" color="primary">
                {isEditMode ? "Update" : "Add Task"}
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default TodoTaskPopup;