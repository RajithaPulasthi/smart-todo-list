import CancelOutlinedIcon from "@mui/icons-material/CancelOutlined";
import * as React from "react";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";

interface LocationPopupProps {
  open: boolean;
  onClose: () => void;
}

const options = ["Option 1", "Option 2"];

const LocationPopup = ({ open, onClose }: LocationPopupProps) => {
  const [value, setValue] = React.useState<string | null>(options[0]);
  const [inputValue, setInputValue] = React.useState("");

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
              inputValue={inputValue}
              onInputChange={(_, newInputValue) => {
                setInputValue(newInputValue);
              }}
              id="controllable-states-demo"
              options={options}
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
          <div className="flex ">
            <button>Save</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LocationPopup;
