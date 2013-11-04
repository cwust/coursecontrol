DROP TABLE IF EXISTS AvaliacaoAluno;
DROP TABLE IF EXISTS Avaliacao;
DROP TABLE IF EXISTS Presenca;
DROP TABLE IF EXISTS Aula;
DROP TABLE IF EXISTS TurmaAluno;
DROP TABLE IF EXISTS TurmaCursoDisciplina;
DROP TABLE IF EXISTS CursoDisciplina;
DROP TABLE IF EXISTS Turma;
DROP TABLE IF EXISTS Localizacao;
DROP TABLE IF EXISTS Disciplina;
DROP TABLE IF EXISTS Curso;
DROP TABLE IF EXISTS Arquivo;
DROP TABLE IF EXISTS Professor;
DROP TABLE IF EXISTS Aluno;
DROP TABLE IF EXISTS Acesso;
DROP TABLE IF EXISTS UsuarioPerfil;
DROP TABLE IF EXISTS Usuario;
DROP TABLE IF EXISTS Perfil;
DROP TABLE IF EXISTS Pessoa;

CREATE TABLE Pessoa (
	codigo	INT NOT NULL PRIMARY KEY,
	nome	VARCHAR(100) NOT NULL,
	sexo CHAR(1),
	cpf	VARCHAR(11),
	email	VARCHAR(100) NOT NULL,
	observacoes	VARCHAR(4000) NOT NULL
);

CREATE TABLE Perfil (
	codigo	INT NOT NULL PRIMARY KEY,
	nome	VARCHAR(30) NOT NULL,
	descricao	VARCHAR(4000)
);


CREATE TABLE Usuario (
	codigo	INT NOT NULL PRIMARY KEY,
	codigoPessoa	INT NOT NULL REFERENCES Pessoa(codigo),
	login	VARCHAR(30) NOT NULL,
	senha	VARCHAR(100)	NOT NULL
);

CREATE TABLE UsuarioPerfil (
	codigoUsuario	INT NOT NULL REFERENCES Usuario(codigo),
	codigoPerfil	INT NOT NULL REFERENCES Perfil(codigo),
	PRIMARY KEY(codigoUsuario, codigoPerfil)
);

CREATE TABLE Acesso (
	codigo	INT NOT NULL PRIMARY KEY,
	login	VARCHAR(100)	NOT NULL,
	horarioAcesso	TIMESTAMP	NOT NULL,
	bemSucedido	BOOLEAN NOT NULL
);

CREATE TABLE Aluno (
	codigo	INT NOT NULL PRIMARY KEY,
	codigoPessoa	INT NOT NULL REFERENCES Pessoa(codigo)
);

CREATE TABLE Professor (
	codigo	INT NOT NULL PRIMARY KEY,
	codigoPessoa	INT NOT NULL REFERENCES Pessoa(codigo)
);

CREATE TABLE Arquivo(
	codigo	INT NOT NULL PRIMARY KEY,
	nome	VARCHAR(100) NOT NULL,
	conteudo BYTEA
);

CREATE TABLE Curso (
	codigo	INT NOT NULL PRIMARY KEY,
	nome	VARCHAR(100) NOT NULL,
	descricao	VARCHAR(4000)
);

CREATE TABLE Disciplina (
	codigo		INT NOT NULL PRIMARY KEY,
	codigoCurso	INT NOT NULL REFERENCES Curso(codigo),
	nome		VARCHAR(100) NOT NULL,
	descricao	VARCHAR(4000),
	duracaoAula INT NOT NULL,
	numeroAulas INT NOT NULL
);

CREATE TABLE Localizacao (
	codigo	INT NOT NULL PRIMARY KEY,
	nome	VARCHAR(100) NOT NULL,
	observacoes	VARCHAR(4000)
);

CREATE TABLE Turma (
	codigo	INT NOT NULL PRIMARY KEY,
	nome	VARCHAR(100),
	dataInicio	DATE,
	concluida	BOOLEAN
);

CREATE TABLE CursoDisciplina (
	codigoCurso	INT NOT NULL REFERENCES Curso(codigo),
	codigoDisciplina	INT NOT NULL REFERENCES Disciplina(codigo),
	PRIMARY KEY (codigoCurso, codigoDisciplina)
);

CREATE TABLE TurmaCursoDisciplina (
	codigoTurma	INT NOT NULL REFERENCES Turma(codigo),
	codigoCurso	INT NOT NULL, 
	codigoDisciplina	INT NOT NULL,
	codigoProfessor	INT NOT NULL REFERENCES Professor(codigo),
	PRIMARY KEY (codigoTurma, codigoCurso, codigoDisciplina),
	FOREIGN KEY (codigoCurso, codigoDisciplina) 
		REFERENCES CursoDisciplina(codigoCurso, codigoDisciplina)
);

CREATE TABLE TurmaAluno (
	codigoTurma	INT NOT NULL REFERENCES Turma(codigo),
	codigoAluno	INT NOT NULL REFERENCES Aluno(codigo),
	PRIMARY KEY (codigoTurma, codigoAluno)
);

CREATE TABLE Aula (
	codigo	INT NOT NULL PRIMARY KEY,
	codigoTurma	INT NOT NULL,
	codigoCurso	INT NOT NULL,
	codigoDisciplina	INT NOT NULL,
	data	DATE NOT NULL,	
	CONSTRAINT FK_Aula_TurmaCursoDisciplina 
		FOREIGN KEY (codigoTurma, codigoCurso, codigoDisciplina)
		REFERENCES TurmaCursoDisciplina(codigoTurma, codigoCurso, codigoDisciplina)
);

CREATE TABLE Presenca (
	codigoAula	INT NOT NULL REFERENCES Aula(codigo),
	codigoAluno	INT NOT NULL REFERENCES Aluno(codigo),
	TIPO	CHAR(1)
);

CREATE TABLE Avaliacao (
	codigo	INT NOT NULL PRIMARY KEY,
	codigoTurma	INT NOT NULL,
	codigoCurso	INT NOT NULL,
	codigoDisciplina	INT NOT NULL,
	codigoArquivo	INT REFERENCES Arquivo(codigo),
	peso	NUMERIC(5,2)	NOT NULL,
	data	DATE,	
	CONSTRAINT FK_Avaliacao_TurmaCursoDisciplina FOREIGN KEY (codigoTurma, codigoCurso, codigoDisciplina)
		REFERENCES TurmaCursoDisciplina(codigoTurma, codigoCurso, codigoDisciplina)
);

CREATE TABLE AvaliacaoAluno (
	codigo_Avaliacao	INT NOT NULL REFERENCES Avaliacao(codigo),
	codigoAluno	INT NOT NULL REFERENCES Avaliacao(codigo),
	nota	NUMERIC(4,2)	NOT NULL,
	observacoes	VARCHAR(4000),
	PRIMARY KEY	(codigo_Avaliacao, codigoAluno)
);

