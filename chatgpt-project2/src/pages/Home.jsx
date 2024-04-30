import React, { useState, useEffect } from 'react';
import Header from '../components/Header';

function Home() {
	const [materia, setMateria] = useState('');
	const [materias, setMaterias] = useState([]);  // Estado separado para materias
	const [assuntos, setAssuntos] = useState([]);
	const [assuntoSelecionado, setAssuntoSelecionado] = useState(''); // Estado para assunto selecionado
	const [resposta, setResposta] = useState(''); // Estado para armazenar a resposta obtida

	useEffect(() => {
		const carregarMaterias = async () => {
			try {
				const response = await fetch(`http://localhost:8080/materias`);
				if (!response.ok) {
					throw new Error('Falha ao carregar matérias');
				}
				const data = await response.json();
				setMaterias(data);
			} catch (error) {
				console.error('Erro ao carregar matérias:', error);
			}
		};

		carregarMaterias();
	}, []);

	useEffect(() => {
		if (materia) {
			const carregarAssuntos = async () => {
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
			};

			carregarAssuntos();
		}
	}, [materia]);

	const gerarResposta = async () => {
		console.log("Assunto selecionado:", assuntoSelecionado);  // Log do assunto selecionado

		if (!assuntoSelecionado) {
			console.error('Nenhum assunto selecionado.');
			return;
		}

		const url = `http://localhost:8080/chat/${assuntoSelecionado}`;
		console.log("URL formada para a requisição:", url);  // Log da URL formada

		try {
			const response = await fetch(url, {
				method: 'GET',
				headers: {
					'Content-Type': 'application/json',
				}
			});

			console.log("Resposta bruta da API:", response);  // Log da resposta bruta

			if (!response.ok) {
				throw new Error('Erro ao obter resposta');
			}

			const result = await response.json();
			console.log("Dados recebidos da API:", result);  // Log dos dados recebidos

			setResposta(result.conteudo); // Atualizar a resposta no estado
		} catch (error) {
			console.error('Erro na requisição:', error);
		}
	};



	return (
		<div className="bg-base-200 flex flex-col gap-10 p-10">
			<Header />
			<div className="flex justify-start items-center gap-2">
				<select className="select select-bordered select-lg w-full max-w-xs"
					value={materia}
					onChange={(e) => setMateria(e.target.value)}>
					<option disabled value="">Selecione uma matéria</option>
					{materias.map((materia) => (
						<option key={materia.id} value={materia.id}>{materia.nome}</option>
					))}
				</select>
			</div>
			<div className="flex justify-start items-center gap-2">
				<select className="select select-bordered select-lg w-full max-w-xs"
					value={assuntoSelecionado}
					onChange={(e) => setAssuntoSelecionado(e.target.value)}>
					<option disabled value="">Selecione um assunto</option>
					{assuntos.map((assunto) => (
						<option key={assunto.id} value={assunto.id}>
							{assunto.nome}
						</option>
					))}
				</select>
				<button className="btn" onClick={gerarResposta}>Gerar</button>
			</div>
			<div className="flex items-center gap-2 h-40 my-10">
				<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" className="w-4 h-4 opacity-70"><path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6ZM12.735 14c.618 0 1.093-.561.872-1.139a6.002 6.002 0 0 0-11.215 0c-.22.578.254 1.139.872 1.139h9.47Z" /></svg>
				<textarea
					className="input input-bordered h-40 w-full p-5"
					value={resposta || "A resposta aparecerá aqui..."}
					onChange={(e) => setResposta(e.target.value)}  // Atualiza o estado quando o usuário digita
				/>


			</div>
		</div>
	);
}

export default Home;
