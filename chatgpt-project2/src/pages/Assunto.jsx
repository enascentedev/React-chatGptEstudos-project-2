import React, { useState, useEffect } from 'react';
import Header from '../components/Header';

function Assunto() {
	const [materias, setMaterias] = useState([]);
	const [assuntos, setAssuntos] = useState([]);
	const [novoAssunto, setNovoAssunto] = useState('');
	const [materiaSelecionada, setMateriaSelecionada] = useState('');
	const [modalVisible, setModalVisible] = useState(false);

	useEffect(() => {
		const fetchMaterias = async () => {
			const response = await fetch('http://localhost:8080/materias');
			const data = await response.json();
			setMaterias(data);
		};

		const fetchAssuntos = async () => {
			const response = await fetch('http://localhost:8080/assuntos');
			const data = await response.json();
			setAssuntos(data);
		};

		fetchMaterias();
		fetchAssuntos();
	}, []);

	const handleNovoAssunto = async (e) => {
		e.preventDefault();
		const response = await fetch('http://localhost:8080/assuntos', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				nome: novoAssunto,
				materia: { id: materiaSelecionada }
			})
		});
		if (response.ok) {
			const addedAssunto = await response.json();
			setAssuntos([...assuntos, addedAssunto]);
			setNovoAssunto('');
			setMateriaSelecionada('');
			setModalVisible(false);
		} else {
			alert("Falha ao adicionar assunto. Tente novamente.");
		}
	};

	const abrirModal = () => setModalVisible(true);
	const fecharModal = () => {
		setModalVisible(false);
		setNovoAssunto('');
		setMateriaSelecionada('');
	};

	return (
		<div className="bg-base-200 flex flex-col gap-10 p-10">
			<Header />
			<button className="btn btn-outline w-40" onClick={abrirModal}>Novo assunto</button>
			{modalVisible && (
				<dialog open className="modal">
					<div className="modal-box">
						<h3>Adicione o assunto: </h3>
						<button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2" onClick={fecharModal}>✕</button>
						<input
							type="text"
							placeholder="Digite o assunto"
							className="input input-bordered w-full my-2"
							value={novoAssunto}
							onChange={(e) => setNovoAssunto(e.target.value)}
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
						<button className="btn btn-primary" onClick={handleNovoAssunto}>Salvar</button>
					</div>
				</dialog>
			)}
			<table className="border-collapse border border-gray-400 w-full">
				<thead>
					<tr>
						<th className="w-14 text-center border border-gray-400">Id</th>
						<th className="px-10 text-start border border-gray-400">Nome</th>
						<th className="px-10 text-start border border-gray-400">Matéria</th>
						<th className="w-40 border border-gray-400">Ação</th>
					</tr>
				</thead>
				<tbody>
					{assuntos.map(assunto => (
						<tr key={assunto.id} className="border-b border-gray-400">
							<td className="text-center border border-gray-400">
								<span>{assunto.id}</span>
							</td>
							<td className="px-10 text-start border border-gray-400">
								<h4>{assunto.nome}</h4>
							</td>
							<td className="px-10 text-start border border-gray-400">
								<h4>{materias.find(m => m.id === assunto.materia.id)?.nome}</h4>
							</td>
							<td className="text-center border border-gray-400 p-2">
								<button className="btn btn-outline w-20 min-h-5 h-8">Editar</button>
							</td>
						</tr>
					))}
				</tbody>
			</table>
		</div>
	);
}

export default Assunto;
