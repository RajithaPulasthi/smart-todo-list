import PlaceIcon from "@mui/icons-material/Place";
import LocationPopup from "./LocationPopup";
import { useState } from "react";

const HeaderSection = () => {
  // const [openLocationPopup, setOpenLocationPopup] = useState(false);
  // const [selectedLocation, setSelectedLocation] = useState("Select Your Current Location");

  // const handleLocationSave = (location: string) => {
  //   setSelectedLocation(location);
  //   setOpenLocationPopup(false);
  // };

  const [isOpen, setIsOpen] = useState(false);
  const [savedLocation, setSavedLocation] = useState<string | null>(
    "Select Your Current Location"
  );

  const handleSaveLocation = (locationName: string) => {
    setSavedLocation(locationName);
    // Do any other necessary operations with the location
  };

  return (
    <div className="w-full h-[12vh] bg-blue-800 flex flex-row items-center">
      <div className="text-5xl text-white basis-4/5 ml-5">
        <h1>Smart Traveling Todo List</h1>
      </div>
      <div className="basis-1/5 flex text-xl text-white ">
        <button className="flex" onClick={() => setIsOpen(true)}>
          <PlaceIcon />
          <h2>{savedLocation}</h2>
        </button>
      </div>
      <LocationPopup
        open={isOpen}
        onClose={() => setIsOpen(false)}
        onSave={handleSaveLocation}
      />
    </div>
  );
};

export default HeaderSection;
