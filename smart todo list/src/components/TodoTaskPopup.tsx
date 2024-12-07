import CancelOutlinedIcon from "@mui/icons-material/CancelOutlined";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import * as React from "react";
import Box from "@mui/material/Box";

interface TodoTaskPopupProps {
  open: boolean;
  onClose: () => void;
}

const options = ["Option 1", "Option 2", "Option 3"];

const TodoTaskPopup = ({ open, onClose }: TodoTaskPopupProps) => {
  const [value, setValue] = React.useState<string | null>(options[0]);
  const [inputValue, setInputValue] = React.useState("");
  return (
    <div
      className={`fixed inset-0 z-50 overflow-y-auto 
        bg-black/50 backdrop-blur-sm 
        flex items-center justify-center 
        p-4 sm:p-6 md:p-8   ${open ? "visible" : "invisible"}`}
    >
      <div className=" bg-blue-700 rounded-2xl">
        <div className="flex justify-end p-4">
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600"
          >
            <CancelOutlinedIcon />
          </button>
        </div>
        <div className="p-4 w-[30vw] h-[40vh]">
          <form action="onclick">
            <div className="sm:col-span-3">
              <label className="block font-medium text-white text-xl place-self-center ">
                What is your task?
              </label>
              <Box
                component="form"
                sx={{ "& > :not(style)": { m: 1, width: "25ch" } }}
                noValidate
                autoComplete="off"
              >
                <TextField
                  id="outlined-basic"
                  label="Task"
                  variant="outlined"
                />
              </Box>
            </div>
            <div>
            
              <div className=" text-white">
              <label className="block font-medium text-white text-xl place-self-center ">
                Task Location?
              </label>
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
            </div>
            <div></div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default TodoTaskPopup;
