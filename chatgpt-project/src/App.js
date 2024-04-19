import "./App.css";


function App() {

	return (
		<div className="p-10">
			<div className="flex flex-col gap-10">
				<select className="select select-bordered select-lg w-full max-w-xs">
					<option disabled selected>JAVA</option>
					<option>JAVA</option>
					<option>React</option>
				</select>
				<select className="select select-bordered select-lg w-full max-w-xs">
					<option disabled selected>JAVA</option>
					<option>Orientação de objetos</option>
					<option>UseState</option>
				</select>
				<button className="btn btn-outline w-40">Enviar</button>
			</div>

			<label class="input input-bordered flex items-center gap-2 h-40 my-10">
				<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" class="w-4 h-4 opacity-70"><path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6ZM12.735 14c.618 0 1.093-.561.872-1.139a6.002 6.002 0 0 0-11.215 0c-.22.578.254 1.139.872 1.139h9.47Z" /></svg>
				<input type="text" class="grow" placeholder="mensagem" />
			</label>
		</div>
	);
}

export default App;