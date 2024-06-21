import { NavLink } from 'react-router-dom';

function Header() {
	return (
		<div className="h-52 flex justify-between items-center bg-black text-white border-2 border-white px-2 rounded-md text-xl">
			<div className="w-full flex flex-1 gap-2 items-center p-2">
				<i className="fa-solid fa-robot text-xl"></i>
				<h2>GERADOR DE PERGUNTAS E RESPOSTAS</h2>
			</div>
			<nav className="flex justify-between gap-5">
				<ul className="flex justify-between gap-5">
					<li>
						<NavLink
							to="/"
							className={({ isActive }) =>
								isActive
									? 'flex items-center bg-gray-800 px-3 py-[22px] '
									: 'flex items-center px-3 py-2 rounded-md hover:bg-gray-700 transition-colors'
							}
						>
							<i className="fa-solid fa-house"></i>
							<span className="ml-2">Home</span>
							{({ isActive }) => isActive && <i className="fa-solid fa-circle text-xs ml-1"></i>}
						</NavLink>
					</li>
					<li>
						<NavLink
							to="/materia"
							className={({ isActive }) =>
								isActive
									? 'flex items-center bg-gray-800 px-3 py-[22px] '
									: 'flex items-center px-3 py-[22px] rounded-md hover:bg-gray-700 transition-colors'
							}
						>
							<i className="fa-solid fa-book"></i>
							<span className="ml-2">Matéria</span>
							{({ isActive }) => isActive && <i className="fa-solid fa-circle text-xs ml-1"></i>}
						</NavLink>
					</li>
					<li>
						<NavLink
							to="/assunto"
							className={({ isActive }) =>
								isActive
									? 'flex items-center bg-gray-800 px-3 py-[22px] '
									: 'flex items-center px-3 py-[22px] rounded-md hover:bg-gray-700 transition-colors'
							}
						>
							<i className="fa-solid fa-chalkboard"></i>
							<span className="ml-2">Assunto</span>
							{({ isActive }) => isActive && <i className="fa-solid fa-circle text-xs ml-1"></i>}
						</NavLink>
					</li>
				</ul>
			</nav>
		</div>
	);
}

export default Header;
