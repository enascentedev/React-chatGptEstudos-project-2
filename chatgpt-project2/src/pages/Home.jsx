import React, { useState, useEffect } from 'react';
import Header from '../components/Header';

function Home() {
	// hooks states 
	const [materia, setMateria] = useState('');
	const [materias, setMaterias] = useState([]);  // Estado separado para materias
	const [assuntos, setAssuntos] = useState([]);
	const [assuntoSelecionado, setAssuntoSelecionado] = useState(''); // Estado para assunto selecionado
	const [resposta, setResposta] = useState(''); // Estado para armazenar a resposta obtida
	const [isLoading, setIsLoading] = useState(false); // Estado de carregamento

	// hook de carregamento materias
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

	// hook de Carregamento de Assuntos Baseado na Matéria Selecionada
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
					setAssuntoSelecionado(''); // Resetar assunto selecionado ao mudar de matéria
				} catch (error) {
					console.error('Erro ao carregar assuntos:', error);
				}
			}
		};

		carregarAssuntos();
	}, [materia]);

	// função que constroi a url de busca e faz o get para a API
	const gerarResposta = async () => {
		console.log("Assunto selecionado:", assuntoSelecionado);  // Log do assunto selecionado

		if (!assuntoSelecionado) {
			console.error('Nenhum assunto selecionado.');
			return;
		}

		const url = `http://localhost:8080/chat/${assuntoSelecionado}`;
		console.log("URL formada para a requisição:", url);  // Log da URL formada

		setIsLoading(true); // Iniciar o estado de carregamento

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
		} finally {
			setIsLoading(false); // Encerrar o estado de carregamento
		}
	};

	return (
		<div className="h-screen flex flex-col gap-10 p-10 bg-blue-800">
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
				<button className="btn w-40 h-16 text-base" onClick={gerarResposta}>Gerar</button>
			</div>
			<div className="flex items-center gap-2 h-40 my-10">
				{isLoading ? (
					<div className="flex items-center">
						<span className="loading loading-ring loading-xs"></span>
						<span className="loading loading-ring loading-sm"></span>
						<span className="loading loading-ring loading-md"></span>
						<span className="loading loading-ring loading-lg"></span>
					</div>
				) : (
					<textarea
						className="input input-bordered h-full w-full p-5"
						value={resposta || "A resposta aparecerá aqui..."}
						onChange={(e) => setResposta(e.target.value)}  // Atualiza o estado quando o usuário digita
					/>
				)}
			</div>
		</div>
	);
}

export default Home;
