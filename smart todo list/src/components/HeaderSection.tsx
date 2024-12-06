import PlaceIcon from "@mui/icons-material/Place";
import LocationPopup from "./LocationPopup";
import { useState } from "react";
const HeaderSection = () => {
  const [openLocationPopup, setOpenLocationPopup] = useState(false);
  return (
    <div className="w-full h-[15vh] bg-blue-800 flex flex-row items-center">
      <div className="text-5xl text-white basis-4/5 ml-5">
        <h1>Smart Traveling Todo List</h1>
      </div>
      <div className="basis-1/5 flex text-xl text-white ">
        <button className="flex" onClick={() => setOpenLocationPopup(true)}>
          <PlaceIcon />
          <h2>Select Location</h2>
        </button>
      </div>
      <LocationPopup
        open={openLocationPopup}
        onClose={() => setOpenLocationPopup(false)}
      />
    </div>
  );
};

export default HeaderSection;
