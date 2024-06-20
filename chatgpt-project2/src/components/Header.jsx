import { NavLink } from 'react-router-dom';

function Header() {
	return (
		<div className=" h-56 flex justify-between items-center bg-black text-white border-2 border-white p-2 rounded-md">
			<div className="w-full flex flex-1 gap-2 items-center p-2">
				<i class="fa-solid fa-robot " ></i>
				<h2>GERADOR DE PERGUNTAS</h2>
			</div>
			<nav className=' flex justify-between gap-5 p-2'>
				<ul className="flex justify-between gap-5">
					<li className={({ isActive }) => isActive ? 'bg-blue-500' : ''}>
						<NavLink to="/" className={({ isActive }) => isActive ? 'underline' : ''}>
							<i class="fa-solid fa-house"></i>
							Home
						</NavLink>
					</li>
					<li>
						<NavLink to="/materia" className={({ isActive }) => isActive ? 'underline' : ''}>
							<i class="fa-solid fa-book"></i>
							Matéria
						</NavLink>
					</li>
					<li>
						<NavLink to="/assunto" className={({ isActive }) => isActive ? 'underline' : ''}>
							<i class="fa-solid fa-chalkboard"></i>
							Assunto
						</NavLink>
					</li>
				</ul>
			</nav>
		</div>
	);
}

export default Header;
