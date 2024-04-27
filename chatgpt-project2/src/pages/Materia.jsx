import Header from '../components/Header';
function Materia() {
	return (
		<div className="bg-base-200 flex flex-col gap-10 p-10">
			<Header></Header>
			<div className="flex justify-start items-center gap-5">
				<h4 className="text-2xl my-2">Id:</h4>
				<input type="text" placeholder="id" className="input input-bordered w-20 max-w-xs" />
				<h4 className="text-2xl my-2">Nome :</h4>
				<input type="text" placeholder="digite o nome" className="input input-bordered w-64 max-w-xs" />
				<button className="btn btn-outline w-28">Consultar</button>
				<button className="btn btn-outline w-28">Nova</button>
			</div>
			<table className="border-collapse border border-gray-400 w-full">
				<thead>
					<th className="w-14 text-center border border-gray-400">Id</th>
					<th className="px-10 text-start ">Nome</th>
					<th className="w-40 border border-gray-400">Ação</th>
				</thead>
				<tbody>
					<tr className="border-b border-gray-400">
						<td className="text-center border border-gray-400">
							<span>1</span>
						</td>
						<td className="px-10 text-start border border-gray-400">
							<h4>Java</h4>
						</td>
						<td className="text-center border border-gray-400 p-2">
							<button className="btn btn-outline w-20 min-h-5 h-8">Editar</button>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	);
}
export default Materia;