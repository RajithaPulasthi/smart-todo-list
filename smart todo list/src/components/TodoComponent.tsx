import Checkbox from "@mui/material/Checkbox";
import PlaceIcon from "@mui/icons-material/Place";

const TodoComponent = () => {
  return (
    <div className="bg-blue-800 rounded-lg w-[80vw] h-[10vh] mt-2 flex flex-row text-white place-items-center">
      <div className="basis-1/12 ml-4">
        <Checkbox sx={{ color: "white" }} />
      </div>
      <div className="basis-7/12 text-2xl ml-[-4vw]">Task Name</div>
      <div className="basis-2/12 flex">
        <PlaceIcon />
        <h2>Select Location</h2>
      </div>
      <div className="basis-2/12">Priority : A</div>
    </div>
  );
};

export default TodoComponent;
