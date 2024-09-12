import getDados from "./getDados.js";

const params = new URLSearchParams(window.location.search);
const serieId = params.get('id');
const listaTemporadas = document.getElementById('temporadas-select');
const fichaSerie = document.getElementById('temporadas-episodios');
const fichaDescricao = document.getElementById('ficha-descricao');

// Função para carregar todas as temporadas
function carregarTemporadas() {
    getDados(`/series/${serieId}/seasons/all`)
        .then(data => {
            const temporadasUnicas = [...new Set(data.map(episodio => episodio.season))]; 
            listaTemporadas.innerHTML = ''; // Limpa as opções existentes

            const optionDefault = document.createElement('option');
            optionDefault.value = '';
            optionDefault.textContent = 'Escolher temporada';
            listaTemporadas.appendChild(optionDefault);

            temporadasUnicas.forEach(season => {
                const option = document.createElement('option');
                option.value = season;
                option.textContent = `Temporada ${season}`;
                listaTemporadas.appendChild(option);
            });

            const optionTodos = document.createElement('option');
            optionTodos.value = 'all';
            optionTodos.textContent = 'Todas as temporadas';
            listaTemporadas.appendChild(optionTodos);
        })
        .catch(error => {
            console.error('Erro ao obter temporadas:', error);
        });
}

// Função para carregar episódios de uma temporada selecionada
function carregarEpisodios() {
    const selectedSeason = listaTemporadas.value;
    getDados(`/series/${serieId}/seasons/${selectedSeason}`)
        .then(data => {
            fichaSerie.innerHTML = '';
            const episodiosPorTemporada = {};

            // Organiza os episódios por temporada
            data.forEach(episode => {
                if (!episodiosPorTemporada[episode.season]) {
                    episodiosPorTemporada[episode.season] = [];
                }
                episodiosPorTemporada[episode.season].push(episode);
            });

            // Exibe os episódios organizados por temporada
            for (const season in episodiosPorTemporada) {
                const ul = document.createElement('ul');
                ul.className = 'episodios-lista';

                const episodiosTemporadaAtual = episodiosPorTemporada[season];

                const listaHTML = episodiosTemporadaAtual.map(episode => `
                    <li>
                        ${episode.episodeNumber} - ${episode.title}
                    </li>
                `).join('');
                ul.innerHTML = listaHTML;

                const paragrafo = document.createElement('p');
                const linha = document.createElement('br');
                paragrafo.textContent = `Temporada ${season}`;
                fichaSerie.appendChild(paragrafo);
                fichaSerie.appendChild(linha);
                fichaSerie.appendChild(ul);
            }
        })
        .catch(error => {
            console.error('Erro ao obter episódios:', error);
        });
}

// Função para carregar informações da série
function carregarInfoSerie() {
    getDados(`/series/${serieId}`)
        .then(data => {
            fichaDescricao.innerHTML = `
                <img src="${data.poster}" alt="${data.title}" />
                <div>
                    <h2>${data.title}</h2>
                    <div class="descricao-texto">
                        <p><b>Média de avaliações:</b> ${data.rating}</p>
                        <p>${data.plot}</p>
                        <p><b>Estrelando:</b> ${data.actors}</p>
                    </div>
                </div>
            `;
        })
        .catch(error => {
            console.error('Erro ao obter informações da série:', error);
        });
}

// Evento para carregar episódios ao trocar a temporada
listaTemporadas.addEventListener('change', carregarEpisodios);

// Carrega as informações da série e as temporadas ao carregar a página
carregarInfoSerie();
carregarTemporadas();
