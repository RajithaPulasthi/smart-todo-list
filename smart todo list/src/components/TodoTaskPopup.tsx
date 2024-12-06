import CancelOutlinedIcon from "@mui/icons-material/CancelOutlined";

interface TodoTaskPopupProps {
  open: boolean;
  onClose: () => void;
}

const TodoTaskPopup = ({ open, onClose }: TodoTaskPopupProps) => {
  return (
    <div
      className={`fixed inset-0 z-50 overflow-y-auto 
        bg-black/50 backdrop-blur-sm 
        flex items-center justify-center 
        p-4 sm:p-6 md:p-8   ${open ? "visible" : "invisible"}`}
    >
      <div
        className="relative  w-full max-w-screen-lg rounded-md
          bg-white
           shadow-2xl 
          border border-gray-200 dark:border-gray-700
          max-h-[90vh] overflow-y-auto
          "
      >
        <div className="flex justify-end p-4">
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600"
          >
            <CancelOutlinedIcon />
          </button>
        </div>
        <div className="p-4">
          <h1 className="text-2xl font-bold">Select Location</h1>
          <p className="text-gray-500">
            Select a location to get the weather and other details
          </p>
        </div>
      </div>
    </div>
  );
};

export default TodoTaskPopup;
