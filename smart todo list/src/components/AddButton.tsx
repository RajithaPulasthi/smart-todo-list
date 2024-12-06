import Box from "@mui/material/Box";
import Fab from "@mui/material/Fab";
import AddIcon from "@mui/icons-material/Add";
import { useState } from "react";
import TodoTaskPopup from "./TodoTaskPopup";

const AddButton = () => {
  const [openTodoTaskPopup, setOpenTodoTaskPopup] = useState(false);
  return (
    <>
      <button onClick={() => setOpenTodoTaskPopup(true)}>
        <Box sx={{ "& > :not(style)": { m: 1 } }}>
          <Fab color="primary" aria-label="add">
            <AddIcon />
          </Fab>
        </Box>
      </button>
      <TodoTaskPopup
        open={openTodoTaskPopup}
        onClose={() => setOpenTodoTaskPopup(false)}
      />
    </>
  );
};

export default AddButton;
