import PlaceIcon from "@mui/icons-material/Place";
import LocationPopup from "./LocationPopup";
import { useState } from "react";

const HeaderSection = () => {
  const [openLocationPopup, setOpenLocationPopup] = useState(false);
  const [selectedLocation, setSelectedLocation] = useState("Option 1"); // Default location

  const handleLocationSave = (location: string) => {
    setSelectedLocation(location); // Update the selected location
    setOpenLocationPopup(false); // Close the popup
  };

  return (
    <div className="w-full h-[15vh] bg-blue-800 flex flex-row items-center">
      <div className="text-5xl text-white basis-4/5 ml-5">
        <h1>Smart Traveling Todo List</h1>
      </div>
      <div className="basis-1/5 flex text-xl text-white ">
        <button className="flex" onClick={() => setOpenLocationPopup(true)}>
          <PlaceIcon />
          <h2>{selectedLocation}</h2> {/* Display the selected location */}
        </button>
      </div>
      <LocationPopup
        open={openLocationPopup}
        onClose={() => setOpenLocationPopup(false)}
        onSaveLocation={handleLocationSave} // Pass the location save handler
        selectedLocation={selectedLocation} // Pass the current selected location
      />
    </div>
  );
};

export default HeaderSection;
