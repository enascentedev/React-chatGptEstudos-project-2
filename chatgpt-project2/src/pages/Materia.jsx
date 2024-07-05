import React, { useState, useEffect } from 'react';

function Materia() {
	const [materias, setMaterias] = useState([]);
	const [nomeMateria, setNomeMateria] = useState('');
	const [modalVisible, setModalVisible] = useState(false);
	const [materiaEmEdicao, setMateriaEmEdicao] = useState(null);
	const [termoPesquisa, setTermoPesquisa] = useState('');
	const [isLoadingMaterias, setIsLoadingMaterias] = useState(false);

	useEffect(() => {
		const fetchMaterias = async () => {
			setIsLoadingMaterias(true);
			try {
				const response = await fetch('https://java-gpt2.onrender.com/materias');
				const data = await response.json();
				setMaterias(data);
			} catch (error) {
				console.error('Erro ao carregar matérias:', error);
			} finally {
				setIsLoadingMaterias(false);
			}
		};

		fetchMaterias();
	}, []);

	const handleSaveMateria = async (e) => {
		e.preventDefault();
		if (!nomeMateria) {
			alert("Por favor, digite o nome da matéria.");
			return;
		}

		const url = materiaEmEdicao
			? `https://java-gpt2.onrender.com/materias/${materiaEmEdicao.id}`
			: 'https://java-gpt2.onrender.com/materias';
		const method = materiaEmEdicao ? 'PUT' : 'POST';
		const body = JSON.stringify({
			id: materiaEmEdicao ? materiaEmEdicao.id : 0,
			nome: nomeMateria,
		});

		const response = await fetch(url, {
			method,
			headers: {
				'Content-Type': 'application/json',
			},
			body,
		});

		if (response.ok) {
			const savedMateria = await response.json();
			setMaterias((prevMaterias) =>
				prevMaterias.some((materia) => materia.id === savedMateria.id)
					? prevMaterias.map((materia) =>
						materia.id === savedMateria.id ? savedMateria : materia
					)
					: [...prevMaterias, savedMateria]
			);
			setNomeMateria('');
			setMateriaEmEdicao(null);
			setModalVisible(false);
		} else {
			alert("Falha ao salvar matéria. Tente novamente.");
		}
	};

	const handleDeleteMateria = async (id) => {
		const url = `https://java-gpt2.onrender.com/materias/${id}`;

		try {
			const response = await fetch(url, {
				method: 'DELETE',
			});

			if (response.ok) {
				setMaterias((prevMaterias) => prevMaterias.filter((materia) => materia.id !== id));
			} else {
				const errorText = await response.text();
				console.error('Error:', errorText);
				alert("Falha ao excluir matéria. Tente novamente.");
			}
		} catch (error) {
			console.error('Network error:', error);
			alert("Erro de rede. Verifique sua conexão.");
		}
	};

	const abrirModal = (materia) => {
		setNomeMateria(materia ? materia.nome : '');
		setMateriaEmEdicao(materia);
		setModalVisible(true);
	};

	const fecharModal = () => {
		setNomeMateria('');
		setMateriaEmEdicao(null);
		setModalVisible(false);
	};

	const handleSearchChange = (e) => {
		setTermoPesquisa(e.target.value.toLowerCase());
	};

	const materiasFiltradas = materias.filter(
		(materia) => materia.nome.toLowerCase().includes(termoPesquisa)
	);

	return (
		<div className="h-screen flex flex-col gap-8 p-10 bg-cover bg-center">
			<div className='flex justify-between gap-2'>
				<input
					type="text"
					placeholder="Digite para pesquisar"
					className="bg-black text-white input input-bordered border-white w-full h-20"
					value={termoPesquisa}
					onChange={handleSearchChange}
				/>
				<button className="btn btn-outline w-32 h-20 bg-black text-white" onClick={() => abrirModal(null)}>Nova matéria</button>
			</div>

			{modalVisible && (
				<dialog open className="modal">
					<div className="modal-box">
						<h3>{materiaEmEdicao ? "Edite a matéria" : "Adicione a matéria"}: </h3>
						<button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2" onClick={fecharModal}>✕</button>
						<input
							type="text"
							placeholder="Digite a matéria"
							className="input input-bordered w-full my-2"
							value={nomeMateria}
							onChange={(e) => setNomeMateria(e.target.value)}
						/>
						<button className="btn btn-primary" onClick={handleSaveMateria}>Salvar</button>
					</div>
				</dialog>
			)}

			{isLoadingMaterias ? (
				<div className="w-full h-full flex flex-col gap-5 justify-center items-center bg-center bg-cover" style={{ backgroundImage: "url('/loading.jpg')" }}>
					<p className="text-white text-xl text font-bold bg-black p-2">Carregando matérias...</p>
				</div>
			) : (
				<table className="border-collapse border bg-black text-white border-white w-full h-80 overflow-y-auto">
					<thead>
						<tr className='h-16'>
							<th className="w-14 text-center border border-white">Id</th>
							<th className="px-10 text-start border border-white">Nome</th>
							<th className="w-40 border border-white">Ação</th>
						</tr>
					</thead>
					<tbody>
						{materiasFiltradas.map((materia) => (
							<tr key={materia.id} className="border-b border-white ">
								<td className="text-center border border-white">
									<span>{materia.id}</span>
								</td>
								<td className="px-10 text-start border border-white">
									<h4>{materia.nome}</h4>
								</td>
								<td className="text-center border border-white p-2 ">
									<button className="btn btn-outline w-20 mx-2 text-white" onClick={() => abrirModal(materia)}>Editar</button>
									<i className="fa-solid fa-trash-can cursor-pointer" onClick={() => handleDeleteMateria(materia.id)}></i>
								</td>
							</tr>
						))}
					</tbody>
				</table>
			)}
		</div>
	);
}

export default Materia;
