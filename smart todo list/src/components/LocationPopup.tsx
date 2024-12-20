import CancelOutlinedIcon from "@mui/icons-material/CancelOutlined";
import * as React from "react";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";

import { useState,useEffect } from "react";

const LocationAPI ='http://localhost:8094/journey-genie-backend-api';

interface LocationPopupProps {
  open: boolean;
  onClose: () => void;
  onSaveLocation: (location: string) => void; 
  selectedLocation: string; 
}

interface LocationData {
  id: number,
        name: string,
        latitude: number,
        longitude: number
}
export default function LocationPopup({
  open,
  onClose,
  onSaveLocation,
  selectedLocation,
}: LocationPopupProps) {
  const [SelectedLocation, setSelectedLocation] = useState<LocationData[]>([]);
  const [value, setValue] = React.useState<string | null>(selectedLocation); // Use selected location as default

  useEffect(() => {
    const fetchLocation = async () => {
      const response = await fetch(`${LocationAPI}/locations`);
      const data = await response.json() as LocationData[];
      setSelectedLocation(data);
    }
    fetchLocation();
  }, []);

  const handleSave = () => {
    if (value) {
      onSaveLocation(value); 
    }
  };

  return (
    <div
      className={`fixed inset-0 z-50 overflow-y-auto 
        bg-black/50 backdrop-blur-sm 
        flex items-center justify-center 
        p-4 sm:p-6 md:p-8   ${open ? "visible" : "invisible"}`}
    >
      <div className="bg-blue-200 rounded-2xl ">
        <div className="flex justify-end p-4">
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600"
          >
            <CancelOutlinedIcon />
          </button>
        </div>
        <div className="p-4">
          <div className="text-xl font-bold">Select Your Current Location</div>
          <div className=" text-white">
            <br />
            <Autocomplete
              value={value}
              onChange={(
                _: React.SyntheticEvent,
                newValue: string | null
              ) => {
                setValue(newValue);
              }}
              id="controllable-states-demo"
              options={SelectedLocation.map((option) => option.name)}
              sx={{ width: 300 }}
              renderInput={(params) => (
                <TextField
                  {...params}
                  label="Select Location"
                  sx={{ bgColor: "white", borderColor: "white" }}
                />
              )}
            />
          </div>
          <div className="flex mt-4 justify-end">
            <button
              onClick={handleSave}
              className="bg-blue-800 text-white px-4 py-2 rounded"
            >
              Save
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}


