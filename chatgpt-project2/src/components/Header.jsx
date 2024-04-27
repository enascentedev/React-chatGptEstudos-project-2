import { Link } from 'react-router-dom';

function Header() {
	return (

		<div className="bg-base-200 flex justify-between">
			<div className="w-full flex flex-1">
				<h2>GERADOR DE PERGUNTAS</h2>
			</div>
			<nav>
				<ul className="flex justify-between gap-5">
					<li><Link to="/" >Home</Link></li>
					<li><Link to="/materia" >Matéria</Link></li>
					<li><Link to="/assunto" >Assunto</Link></li>
				</ul>
			</nav>
		</div>

	);
}
export default Header;