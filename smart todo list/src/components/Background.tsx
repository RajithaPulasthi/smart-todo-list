import AddButton from "./AddButton"
import TodoComponent from "./TodoComponent"

const Background = () => {
  return (
    <div className='w-full h-[75vh] bg-blue-600 flex flex-col'>
        <div className="justify-center flex basis-5/6"><TodoComponent /></div>
        <div className="place-self-end basis-1/6"><AddButton    /></div>
      
      
    </div>
  )
}

export default Background
