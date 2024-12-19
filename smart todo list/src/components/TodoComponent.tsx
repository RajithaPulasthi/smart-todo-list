import Checkbox from "@mui/material/Checkbox";
import PlaceIcon from "@mui/icons-material/Place";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";

interface TodoComponentProps {
  taskName: string;
  location: string;
  priority: string;
  isChecked: boolean;
  onCheckChange: (checked: boolean) => void;
  onEditClick: () => void;
  onDeleteClick: () => void;
}

const TodoComponent = ({
  taskName,
  location,
  priority,
  isChecked,
  onCheckChange,
  onEditClick,
  onDeleteClick,
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
      <div className="basis-1/12 flex space-x-2">
        <EditIcon onClick={onEditClick} className="cursor-pointer" />
        <DeleteIcon onClick={onDeleteClick} className="cursor-pointer" />
      </div>
    </div>
  );
};

export default TodoComponent;
