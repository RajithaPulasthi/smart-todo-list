import CancelOutlinedIcon from "@mui/icons-material/CancelOutlined";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import { useState, useEffect } from "react";

const LocationAPI = "http://localhost:8094/journey-genie-backend-api/locations";

interface LocationPopupProps {
  open: boolean;
  onClose: () => void;
  onSave: (locationName: string) => void;
}

interface LocationDataProps {
  id: number;
  name: string;
  latitude: number;
  longitude: number;
}

const LocationPopup = ({ open, onClose, onSave }: LocationPopupProps) => {
  const [locationData, setLocationData] = useState<LocationDataProps[]>([]);
  const [selectedLocation, setSelectedLocation] = useState<string | null>(null);

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

  const handleSave = () => {
    if (selectedLocation) {
      onSave(selectedLocation);
      setSelectedLocation(null);
      onClose();
    }
  };

  return (
    <div
      className={`fixed inset-0 z-50 overflow-y-auto bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 sm:p-6 md:p-8 ${
        open ? "visible" : "invisible"
      }`}
    >
      <div className="bg-blue-600 rounded-2xl">
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
          <div className="text-white">
            <br />
            <Autocomplete
              value={selectedLocation}
              onChange={(_, newValue) => setSelectedLocation(newValue)}
              options={locationData.map((option) => option.name)}
              sx={{ width: 300 }}
              renderInput={(params) => (
                <TextField
                  {...params}
                  label="Select Location"
                  sx={{ bgcolor: "white" }}
                />
              )}
            />
          </div>
          <div className="flex mt-4 justify-end">
            <button
              className="bg-blue-800 text-white px-4 py-2 rounded"
              onClick={handleSave}
            >
              Save
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LocationPopup;