import { NavLink } from 'react-router-dom';

function Header() {
	return (
		<div className="bg-base-200 flex justify-between">
			<div className="w-full flex flex-1">
				<h2>GERADOR DE PERGUNTAS</h2>
			</div>
			<nav>
				<ul className="flex justify-between gap-5">
					<li>
						<NavLink to="/" className={({ isActive }) => isActive ? 'underline' : ''}>
							Home
						</NavLink>
					</li>
					<li>
						<NavLink to="/materia" className={({ isActive }) => isActive ? 'underline' : ''}>
							Matéria
						</NavLink>
					</li>
					<li>
						<NavLink to="/assunto" className={({ isActive }) => isActive ? 'underline' : ''}>
							Assunto
						</NavLink>
					</li>
				</ul>
			</nav>
		</div>
	);
}

export default Header;
