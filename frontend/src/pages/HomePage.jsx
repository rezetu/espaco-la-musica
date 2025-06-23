import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card.jsx'
import { Users, BookOpen, UserCheck, TrendingUp } from 'lucide-react'

function HomePage() {
  const [stats, setStats] = useState({
    totalPessoas: 0,
    totalCursos: 0,
    totalMatriculas: 0,
    cursosAtivos: 0
  })

  useEffect(() => {
    // Buscar estatísticas das APIs
    fetchStats()
  }, [])

  const fetchStats = async () => {
    try {
      const [pessoasRes, cursosRes, matriculasRes] = await Promise.all([
        fetch('http://localhost:8080/api/pessoas'),
        fetch('http://localhost:8080/api/cursos'),
        fetch('http://localhost:8080/api/matriculas')
      ])

      const pessoas = await pessoasRes.json()
      const cursos = await cursosRes.json()
      const matriculas = await matriculasRes.json()

      const cursosAtivos = cursos.filter(curso => curso.ativo).length

      setStats({
        totalPessoas: pessoas.length,
        totalCursos: cursos.length,
        totalMatriculas: matriculas.length,
        cursosAtivos
      })
    } catch (error) {
      console.error('Erro ao buscar estatísticas:', error)
    }
  }

  const statCards = [
    {
      title: 'Total de Pessoas',
      value: stats.totalPessoas,
      description: 'Pessoas cadastradas no sistema',
      icon: Users,
      color: 'text-blue-600'
    },
    {
      title: 'Total de Cursos',
      value: stats.totalCursos,
      description: 'Cursos disponíveis',
      icon: BookOpen,
      color: 'text-green-600'
    },
    {
      title: 'Cursos Ativos',
      value: stats.cursosAtivos,
      description: 'Cursos atualmente ativos',
      icon: TrendingUp,
      color: 'text-purple-600'
    },
    {
      title: 'Total de Matrículas',
      value: stats.totalMatriculas,
      description: 'Matrículas realizadas',
      icon: UserCheck,
      color: 'text-orange-600'
    }
  ]

  return (
    <div className="px-4 py-6 sm:px-0">
      <div className="border-4 border-dashed border-gray-200 rounded-lg p-6">
        <div className="text-center mb-8">
              <h1 className="text-3xl font-bold text-gray-900">
            Bem-vindo ao Espaço La Música
          </h1>>
          <p className="text-lg text-gray-600">
            Gerencie pessoas, cursos e matrículas de forma eficiente
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          {statCards.map((stat, index) => {
            const Icon = stat.icon
            return (
              <Card key={index} className="hover:shadow-lg transition-shadow">
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                  <CardTitle className="text-sm font-medium">
                    {stat.title}
                  </CardTitle>
                  <Icon className={`h-4 w-4 ${stat.color}`} />
                </CardHeader>
                <CardContent>
                  <div className="text-2xl font-bold">{stat.value}</div>
                  <p className="text-xs text-muted-foreground">
                    {stat.description}
                  </p>
                </CardContent>
              </Card>
            )
          })}
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center">
                <Users className="w-5 h-5 mr-2 text-blue-600" />
                Pessoas
              </CardTitle>
              <CardDescription>
                Gerencie o cadastro de alunos, professores e funcionários
              </CardDescription>
            </CardHeader>
            <CardContent>
              <p className="text-sm text-gray-600">
                Cadastre e mantenha informações atualizadas de todas as pessoas do sistema.
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle className="flex items-center">
                <BookOpen className="w-5 h-5 mr-2 text-green-600" />
                Cursos
              </CardTitle>
              <CardDescription>
                Administre os cursos oferecidos pela instituição
              </CardDescription>
            </CardHeader>
            <CardContent>
              <p className="text-sm text-gray-600">
                Crie, edite e gerencie todos os cursos disponíveis para matrícula.
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle className="flex items-center">
                <UserCheck className="w-5 h-5 mr-2 text-orange-600" />
                Matrículas
              </CardTitle>
              <CardDescription>
                Controle as matrículas e pagamentos
              </CardDescription>
            </CardHeader>
            <CardContent>
              <p className="text-sm text-gray-600">
                Realize matrículas, acompanhe pagamentos e gerencie o status dos alunos.
              </p>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  )
}

export default HomePage

