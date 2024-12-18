import Checkbox from "@mui/material/Checkbox";
import PlaceIcon from "@mui/icons-material/Place";

interface TodoComponentProps {
  taskName: string;
  location: string;
  priority: string;
  isChecked: boolean;
  onCheckChange: (checked: boolean) => void;
}

const TodoComponent = ({
  taskName,
  location,
  priority,
  isChecked,
  onCheckChange,
}: TodoComponentProps) => {
  return (
    <div className="bg-blue-800 rounded-lg w-[80vw] h-[10vh] mt-2 flex flex-row text-white place-items-center">
      <div className="basis-1/12 ml-4">
        <Checkbox
          checked={isChecked}
          onChange={(e) => onCheckChange(e.target.checked)}
          sx={{ color: "white" }}
        />
      </div>
      <div className="basis-7/12 text-2xl ml-[-4vw]">{taskName}</div>
      <div className="basis-2/12 flex">
        <PlaceIcon />
        <h2>{location}</h2>
      </div>
      <div className="basis-2/12">Priority: {priority}</div>
    </div>
  );
};

export default TodoComponent;
