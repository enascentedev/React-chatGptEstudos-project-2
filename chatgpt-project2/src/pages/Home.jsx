import React, { useState, useEffect } from 'react';

function Home() {
  // hooks states 
  const [materia, setMateria] = useState('');
  const [materias, setMaterias] = useState([]);  
  const [assuntos, setAssuntos] = useState([]);
  const [assuntoSelecionado, setAssuntoSelecionado] = useState(''); 
  const [resposta, setResposta] = useState(''); 
  const [isLoadingMaterias, setIsLoadingMaterias] = useState(false);
  const [isLoadingAssuntos, setIsLoadingAssuntos] = useState(false); 
  const [isLoadingResposta, setIsLoadingResposta] = useState(false); 

  // hook de carregamento materias
  useEffect(() => {
    const carregarMaterias = async () => {
      setIsLoadingMaterias(true); // Iniciar o estado de carregamento de materias
      try {
        const response = await fetch(`https://java-gpt2.onrender.com/materias`);
        if (!response.ok) {
          throw new Error('Falha ao carregar matérias');
        }
        const data = await response.json();
        setMaterias(data);
      } catch (error) {
        console.error('Erro ao carregar matérias:', error);
      } finally {
        setIsLoadingMaterias(false); // Encerrar o estado de carregamento de materias
      }
    };

    carregarMaterias();
  }, []);

  // hook de Carregamento de Assuntos Baseado na Matéria Selecionada
  useEffect(() => {
    const carregarAssuntos = async () => {
      if (materia) {
        setIsLoadingAssuntos(true); // Iniciar o estado de carregamento de assuntos
        try {
          const response = await fetch(`https://java-gpt2.onrender.com/assuntos/${materia}`);
          if (!response.ok) {
            throw new Error('Falha ao carregar assuntos');
          }
          const data = await response.json();
          setAssuntos(data);
          setAssuntoSelecionado(''); // Resetar assunto selecionado ao mudar de matéria
        } catch (error) {
          console.error('Erro ao carregar assuntos:', error);
        } finally {
          setIsLoadingAssuntos(false); // Encerrar o estado de carregamento de assuntos
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

    const url = `https://java-gpt2.onrender.com/chat/${assuntoSelecionado}`;
    console.log("URL formada para a requisição:", url);  // Log da URL formada

    setIsLoadingResposta(true); // Iniciar o estado de carregamento da resposta

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
      setIsLoadingResposta(false); // Encerrar o estado de carregamento da resposta
    }
  };

  return (
    <div className="h-screen overflow-y-auto flex flex-col gap-10 p-10 bg-cover bg-center">
      <div className="flex justify-start items-center gap-2">
        <select
          className="select select-bordered select-lg w-full max-w-xs bg-black text-white border-white"
          value={materia}
          onChange={(e) => setMateria(e.target.value)}
          disabled={isLoadingMaterias || isLoadingResposta}
        >
          <option disabled value="">
            Selecione uma matéria
          </option>
          {isLoadingMaterias ? (
            <option disabled>Carregando...</option>
          ) : (
            materias.map((materia) => (
              <option key={materia.id} value={materia.id}>
                {materia.nome}
              </option>
            ))
          )}
        </select>
      </div>
      <div className="flex-row justify-start items-center gap-10 sm:flex-col">
        <select
          className="select select-bordered select-lg w-full max-w-xs bg-black text-white border-white"
          value={assuntoSelecionado}
          onChange={(e) => setAssuntoSelecionado(e.target.value)}
          disabled={isLoadingAssuntos || isLoadingResposta || isLoadingMaterias}
        >
          <option disabled value="">
            Selecione um assunto
          </option>
          {isLoadingAssuntos ? (
            <option disabled>
              <span className="loading loading-spinner loading-xs"></span>
              <span className="loading loading-spinner loading-sm"></span>
              <span className="loading loading-spinner loading-md"></span>
              <span className="loading loading-spinner loading-lg"></span>
            </option>
          ) : (
            assuntos.map((assunto) => (
              <option key={assunto.id} value={assunto.id}>
                {assunto.nome}
              </option>
            ))
          )}
        </select>
        <button
          className="btn sm:w-40 w-full h-16 text-base bg-black text-white sm:mx-5 max-w-xs sm:my-0 my-10"
          onClick={gerarResposta}
          disabled={isLoadingResposta}
        >
          Gerar
        </button>
      </div>
      <div className="flex gap-2 h-full ">
        {isLoadingResposta ? (
          <div className="w-full h-full flex flex-col text-white gap-5 ">
					<div>
						<span class="loading loading-spinner loading-lg"></span>
						<span class="loading loading-spinner loading-lg"></span>
						<span class="loading loading-spinner loading-lg"></span>
						<span class="loading loading-spinner loading-lg"></span>
					</div>
					<p className="text-white text-xl text font-bold bg-black p-2">Carregando resposta...</p>
				</div>
        ) : (
          <textarea
            className="input input-bordered h-72 w-full p-5 bg-black text-white border-spacing-2 border-white"
            value={resposta || "A resposta aparecerá aqui..."}
            onChange={(e) => setResposta(e.target.value)} // Atualiza o estado quando o usuário digita
          />
        )}
      </div>
    </div>
  );
}

export default Home;
