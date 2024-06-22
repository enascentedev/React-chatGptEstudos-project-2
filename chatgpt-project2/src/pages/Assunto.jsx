import React, { useState, useEffect } from 'react';

function Assunto() {
	const [materias, setMaterias] = useState([]);
	const [assuntos, setAssuntos] = useState([]);
	const [nomeAssunto, setNomeAssunto] = useState('');
	const [materiaSelecionada, setMateriaSelecionada] = useState('');
	const [modalVisible, setModalVisible] = useState(false);
	const [assuntoEmEdicao, setAssuntoEmEdicao] = useState(null);
	const [termoPesquisa, setTermoPesquisa] = useState('');
	
	const [isLoadingAssuntos, setIsLoadingAssuntos] = useState(false);

	useEffect(() => {
		const fetchMaterias = async () => {
			
			try {
				const response = await fetch('https://java-gpt2.onrender.com/materias');
				const data = await response.json();
				setMaterias(data);
			} catch (error) {
				console.error('Erro ao carregar matérias:', error);
			} 
		};

		const fetchAssuntos = async () => {
			setIsLoadingAssuntos(true);
			try {
				const response = await fetch('https://java-gpt2.onrender.com/assuntos');
				const data = await response.json();
				setAssuntos(data);
			} catch (error) {
				console.error('Erro ao carregar assuntos:', error);
			} finally {
				setIsLoadingAssuntos(false);
			}
		};

		fetchMaterias();
		fetchAssuntos();
	}, []);

	const handleSaveAssunto = async (e) => {
		e.preventDefault();
		const assuntoId = assuntoEmEdicao ? assuntoEmEdicao.id : null;
		const url = assuntoEmEdicao ? `https://java-gpt2.onrender.com/assuntos/${assuntoId}` : `https://java-gpt2.onrender.com/assuntos`;
		const method = assuntoEmEdicao ? 'PUT' : 'POST';
		const body = JSON.stringify({
			id: assuntoId,
			nome: nomeAssunto,
			materia: { id: parseInt(materiaSelecionada) }
		});

		try {
			const response = await fetch(url, {
				method,
				headers: {
					'Content-Type': 'application/json',
				},
				body,
			});

			if (response.ok) {
				const savedAssunto = await response.json();
				setAssuntos((prevAssuntos) =>
					prevAssuntos.some((assunto) => assunto.id === savedAssunto.id)
						? prevAssuntos.map((assunto) =>
							assunto.id === savedAssunto.id ? savedAssunto : assunto
						)
						: [...prevAssuntos, savedAssunto]
				);
				resetModal();
			} else {
				const errorText = await response.text();
				console.error('Error:', errorText);
				alert("Falha ao salvar assunto. Tente novamente.");
			}
		} catch (error) {
			console.error('Network error:', error);
			alert("Erro de rede. Verifique sua conexão.");
		}
	};

	const handleDeleteAssunto = async (id) => {
		const url = `https://java-gpt2.onrender.com/assuntos/${id}`;

		try {
			const response = await fetch(url, {
				method: 'DELETE',
			});

			if (response.ok) {
				setAssuntos((prevAssuntos) => prevAssuntos.filter((assunto) => assunto.id !== id));
			} else {
				const errorText = await response.text();
				console.error('Error:', errorText);
				alert("Falha ao excluir assunto. Tente novamente.");
			}
		} catch (error) {
			console.error('Network error:', error);
			alert("Erro de rede. Verifique sua conexão.");
		}
	};

	const abrirModal = (assunto) => {
		setNomeAssunto(assunto ? assunto.nome : '');
		setMateriaSelecionada(assunto ? assunto.materia.id.toString() : '');
		setAssuntoEmEdicao(assunto);
		setModalVisible(true);
	};

	const fecharModal = () => resetModal();

	const resetModal = () => {
		setNomeAssunto('');
		setMateriaSelecionada('');
		setAssuntoEmEdicao(null);
		setModalVisible(false);
	};

	const handleSearchChange = (e) => {
		setTermoPesquisa(e.target.value.toLowerCase());
	};

	const assuntosFiltrados = assuntos.filter(
		(assunto) =>
			assunto.nome.toLowerCase().includes(termoPesquisa) ||
			materias.find((materia) => materia.id === assunto.materia.id)?.nome.toLowerCase().includes(termoPesquisa)
	);

	return (
		<div className="h-screen flex flex-col gap-8 md:p-10 p-5 bg-cover bg-center">
			<div className='flex justify-between gap-2'>
				<input
					type="text"
					placeholder="Digite para pesquisar"
					className="bg-black text-white input input-bordered w-full h-20"
					value={termoPesquisa}
					onChange={handleSearchChange}
				/>
				<button className="btn btn-outline w-20 h-20 bg-black text-white" onClick={() => abrirModal(null)}>Novo assunto</button>
			</div>

			{modalVisible && (
				<dialog open className="modal">
					<div className="modal-box">
						<h3>{assuntoEmEdicao ? "Edite o assunto" : "Adicione o assunto"}: </h3>
						<button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2" onClick={fecharModal}>✕</button>
						<input
							type="text"
							placeholder="Digite o assunto"
							className="input input-bordered w-full my-2"
							value={nomeAssunto}
							onChange={(e) => setNomeAssunto(e.target.value)}
						/>
						<select
							className="select select-bordered w-full my-2"
							value={materiaSelecionada}
							onChange={(e) => setMateriaSelecionada(e.target.value)}
						>
							<option value="">Selecione uma matéria</option>
							{materias.map((materia) => (
								<option key={materia.id} value={materia.id}>{materia.nome}</option>
							))}
						</select>
						<button className="btn btn-primary" onClick={handleSaveAssunto}>Salvar</button>
					</div>
				</dialog>
			)}

			{isLoadingAssuntos ? (
				<div className="w-full h-full flex flex-col gap-5 justify-center items-center bg-center bg-cover" style={{ backgroundImage: "url('/loading.jpg')" }}>
					<p className="text-white text-xl text font-bold bg-black p-2">Carregando assuntos...</p>
					
				</div>
			) : (
				<table className="border-collapse border border-gray-400 w-full bg-black text-white">
					<thead>
						<tr className='h-16'>
							<th className="w-14 md:px-10 px-2 text-center border border-gray-400">Id</th>
							<th className="md:w-40 w-20 md:px-10 px-2 text-start border border-gray-400">Nome</th>
							<th className="md:w-40 w-20 md:px-10 px-2 text-start border border-gray-400">Matéria</th>
							<th className="w-10 border border-gray-400">Ação</th>
						</tr>
					</thead>
					<tbody>
						{assuntosFiltrados.map((assunto) => (
							<tr key={assunto.id} className="border-b border-gray-400">
								<td className="md:px-10 px-4 text-center border border-gray-400">
									<span>{assunto.id}</span>
								</td>
								<td className="md:px-10 px-4 text-start border border-gray-400">
									<h4>{assunto.nome}</h4>
								</td>
								<td className="md:px-10 px-4 text-start border border-gray-400">
									<h4>{materias.find((m) => m.id === assunto.materia.id)?.nome}</h4>
								</td>
								<td className="text-center border  border-gray-400 md:px-50 px-0">
									<button className="text-white btn btn-outline w-20 min-h-5 h-8 mx-2" onClick={() => abrirModal(assunto)}>Editar</button>
									<i className="fa-solid fa-trash-can cursor-pointer" onClick={() => handleDeleteAssunto(assunto.id)}></i>
								</td>
							</tr>
						))}
					</tbody>
				</table>
			)}
		</div>
	);
}

export default Assunto;
