import React, { useState, useEffect } from 'react';
import Header from '../components/Header';

function Materia() {
	const [materias, setMaterias] = useState([]);
	const [nomeMateria, setNomeMateria] = useState('');
	const [modalVisible, setModalVisible] = useState(false);
	const [materiaEmEdicao, setMateriaEmEdicao] = useState(null);

	useEffect(() => {
		const fetchMaterias = async () => {
			const response = await fetch('http://localhost:8080/materias');
			const data = await response.json();
			setMaterias(data);
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
			? `http://localhost:8080/materias/${materiaEmEdicao.id}`
			: 'http://localhost:8080/materias';
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
			if (materiaEmEdicao) {
				setMaterias((prevMaterias) =>
					prevMaterias.map((materia) =>
						materia.id === savedMateria.id ? savedMateria : materia
					)
				);
			} else {
				setMaterias((prevMaterias) => [...prevMaterias, savedMateria]);
			}
			setNomeMateria('');
			setMateriaEmEdicao(null);
			setModalVisible(false);
		} else {
			alert("Falha ao salvar matéria. Tente novamente.");
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

	return (
		<div className="bg-base-200 flex flex-col gap-10 p-10">
			<Header></Header>

			<button className="btn btn-outline w-40" onClick={() => abrirModal(null)}>Nova matéria</button>

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

			<table className="border-collapse border border-gray-400 w-full">
				<thead>
					<tr>
						<th className="w-14 text-center border border-gray-400">Id</th>
						<th className="px-10 text-start border border-gray-400">Nome</th>
						<th className="w-40 border border-gray-400">Ação</th>
					</tr>
				</thead>
				<tbody>
					{materias.map((materia) => (
						<tr key={materia.id} className="border-b border-gray-400">
							<td className="text-center border border-gray-400">
								<span>{materia.id}</span>
							</td>
							<td className="px-10 text-start border border-gray-400">
								<h4>{materia.nome}</h4>
							</td>
							<td className="text-center border border-gray-400 p-2">
								<button className="btn btn-outline w-20" onClick={() => abrirModal(materia)}>Editar</button>
							</td>
						</tr>
					))}
				</tbody>
			</table>
		</div>
	);
}

export default Materia;
