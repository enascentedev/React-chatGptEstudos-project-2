import "../assets/main.css";
import React from 'react';
import Header from '../components/Header';

function Home() {

	return (
		<div className="bg-base-200 flex flex-col gap-10 p-10">
			<Header></Header>
			<div className="flex justify-start items-center gap-2">
				<select className="select select-bordered select-lg w-full max-w-xs">
					<option disabled selected>JAVA</option>
					<option>JAVA</option>
					<option>React</option>
				</select>
				<button className="btn" onClick={() => document.getElementById('my_modal_3').showModal()}>+</button>
				<dialog id="my_modal_3" className="modal h-3/4">
					<div className="modal-box h-96 flex flex-col">
						<form method="dialog">
							<button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
						</form>
						<div className=" p-5">
							<h3 className=" bg-base-300 text-center text-2xl mb-2">Novo Assunto</h3>
							<h4 className=" bg-base-300 text-2xl my-2">Matéria :</h4>
							<select className="select select-bordered select-lg w-full max-w-xs py-2">
								<option disabled selected>JAVA</option>
								<option>JAVA</option>
								<option>React</option>
							</select>
							<h4 className=" bg-base-300 text-2xl my-2">Assunto :</h4>
							<input type="text" placeholder="digite o assunto" className="input input-bordered w-full max-w-xs" />
						</div>
						<div className="flex justify-around">
							<button className="btn btn-outline w-20">Cancelar</button>
							<button className="btn btn-outline w-20">Salvar</button>
						</div>
					</div>
				</dialog>
			</div>
			<div className="flex justify-start items-center gap-2">
				<select className="select select-bordered select-lg w-full max-w-xs">
					<option disabled selected>JAVA</option>
					<option>JAVA</option>
					<option>React</option>
				</select>
				<button className="btn" onClick={() => document.getElementById('my_modal_4').showModal()}>+</button>
				<dialog id="my_modal_4" className="modal h-3/4">
					<div className="modal-box h-72 flex flex-col">
						<form method="dialog">
							<button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
						</form>
						<div className=" p-5">
							<h3 className=" bg-base-300 text-center text-2xl mb-2">Nova matéria</h3>
							<h4 className=" bg-base-300 text-2xl my-2">Assunto :</h4>
							<input type="text" placeholder="digite o assunto" className="input input-bordered w-full max-w-xs" />
						</div>
						<div className="flex justify-around">
							<button className="btn btn-outline w-20">Cancelar</button>
							<button className="btn btn-outline w-20">Salvar</button>
						</div>
					</div>
				</dialog>
			</div>

			<button className="btn btn-outline w-40">Gerar</button>


			<label class="input input-bordered flex items-center gap-2 h-40 my-10">
				<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" class="w-4 h-4 opacity-70"><path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6ZM12.735 14c.618 0 1.093-.561.872-1.139a6.002 6.002 0 0 0-11.215 0c-.22.578.254 1.139.872 1.139h9.47Z" /></svg>
				<input type="text" class="grow" placeholder="Resposta" />
			</label>

		</div>
	);
}

export default Home;