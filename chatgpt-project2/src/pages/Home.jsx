import React, { useState, useEffect } from 'react';
import Header from '../components/Header';

function Home() {
	const [materia, setMateria] = useState('');
	const [assuntos, setAssuntos] = useState([]);
	const [assuntoSelecionado, setAssuntoSelecionado] = useState('');
	const [resposta, setResposta] = useState('');

	useEffect(() => {
		const carregarAssuntos = async () => {
			if (materia) {
				try {
					const response = await fetch(`http://localhost:8080/assuntos/${materia}`);
					if (!response.ok) {
						throw new Error('Falha ao carregar assuntos');
					}
					const data = await response.json();
					setAssuntos(data);
				} catch (error) {
					console.error('Erro ao carregar assuntos:', error);
				}
			}
		};

		carregarAssuntos();
	}, [materia]);

	const gerarResposta = async () => {
		try {
			const response = await fetch('http://localhost:8080/chat', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({ descricao: assuntoSelecionado })
			});
			if (!response.ok) {
				throw new Error('Erro ao obter resposta');
			}
			const result = await response.json();
			setResposta(result.descricao);  // Atualizar a resposta no estado
		} catch (error) {
			console.error('Erro:', error);
		}
	};

	return (
		<div className="bg-base-200 flex flex-col gap-10 p-10">
			<Header />
			<div className="flex justify-start items-center gap-2">
				<select className="select select-bordered select-lg w-full max-w-xs"
					value={materia}
					onChange={e => setMateria(e.target.value)}>
					<option disabled selected>Selecione uma matéria</option>
					<option value="java">JAVA</option>
					<option value="react">React</option>
				</select>
			</div>
			<div className="flex justify-start items-center gap-2">
				<select className="select select-bordered select-lg w-full max-w-xs"
					value={assuntoSelecionado}
					onChange={e => setAssuntoSelecionado(e.target.value)}>
					<option disabled selected>Selecione um assunto</option>
					{assuntos.map((assunto, index) => (
						<option key={index} value={assunto.descricao}>{assunto.descricao}</option>
					))}
				</select>
				<button className="btn" onClick={gerarResposta}>Gerar</button>
			</div>
			<div className="flex items-center gap-2 h-40 my-10">
				<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" className="w-4 h-4 opacity-70"><path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6ZM12.735 14c.618 0 1.093-.561.872-1.139a6.002 6.002 0 0 0-11.215 0c-.22.578.254 1.139.872 1.139h9.47Z" /></svg>
				<label className="input input-bordered w-full">{resposta || "A resposta aparecerá aqui..."}</label>
			</div>
		</div>
	);
}

export default Home;
