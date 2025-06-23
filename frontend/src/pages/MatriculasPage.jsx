import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button.jsx'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card.jsx'
import { Input } from '@/components/ui/input.jsx'
import { Label } from '@/components/ui/label.jsx'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select.jsx'
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog.jsx'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table.jsx'
import { Badge } from '@/components/ui/badge.jsx'
import { Plus, Edit, Trash2, Search, UserCheck, Calendar } from 'lucide-react'

function MatriculasPage() {
  const [matriculas, setMatriculas] = useState([])
  const [pessoas, setPessoas] = useState([])
  const [cursos, setCursos] = useState([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [editingMatricula, setEditingMatricula] = useState(null)
  const [formData, setFormData] = useState({
    alunoId: '',
    cursoId: '',
    valorCobrado: '',
    dataVencimento: ''
  })

  useEffect(() => {
    fetchData()
  }, [])

  const fetchData = async () => {
    try {
      setLoading(true)
      const [matriculasRes, pessoasRes, cursosRes] = await Promise.all([
        fetch('http://localhost:8080/api/matriculas'),
        fetch('http://localhost:8080/api/pessoas'),
        fetch('http://localhost:8080/api/cursos')
      ])

      const matriculasData = await matriculasRes.json()
      const pessoasData = await pessoasRes.json()
      const cursosData = await cursosRes.json()

      setMatriculas(matriculasData)
      setPessoas(pessoasData)
      setCursos(cursosData.filter(curso => curso.ativo))
    } catch (error) {
      console.error('Erro ao buscar dados:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      const url = editingMatricula 
        ? `http://localhost:8080/api/matriculas/${editingMatricula.id}/status-pagamento`
        : 'http://localhost:8080/api/matriculas'
      
      const method = editingMatricula ? 'PATCH' : 'POST'
      
      let body
      if (editingMatricula) {
        // Para edição, apenas enviamos o status de pagamento
        body = JSON.stringify({ statusPagamento: formData.statusPagamento })
      } else {
        // Para nova matrícula
        body = JSON.stringify({
          alunoId: parseInt(formData.alunoId),
          cursoId: parseInt(formData.cursoId),
          valorCobrado: parseFloat(formData.valorCobrado),
          dataVencimento: formData.dataVencimento
        })
      }
      
      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
        },
        body,
      })

      if (response.ok) {
        await fetchData()
        setIsDialogOpen(false)
        resetForm()
      } else {
        console.error('Erro ao salvar matrícula')
      }
    } catch (error) {
      console.error('Erro ao salvar matrícula:', error)
    }
  }

  const handleEdit = (matricula) => {
    setEditingMatricula(matricula)
    setFormData({
      alunoId: matricula.aluno.id.toString(),
      cursoId: matricula.curso.id.toString(),
      valorCobrado: matricula.valorCobrado.toString(),
      dataVencimento: matricula.dataVencimento,
      statusPagamento: matricula.statusPagamento
    })
    setIsDialogOpen(true)
  }

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja cancelar esta matrícula?')) {
      try {
        const response = await fetch(`http://localhost:8080/api/matriculas/${id}`, {
          method: 'DELETE',
        })

        if (response.ok) {
          await fetchData()
        } else {
          console.error('Erro ao cancelar matrícula')
        }
      } catch (error) {
        console.error('Erro ao cancelar matrícula:', error)
      }
    }
  }

  const resetForm = () => {
    setFormData({
      alunoId: '',
      cursoId: '',
      valorCobrado: '',
      dataVencimento: ''
    })
    setEditingMatricula(null)
  }

  const filteredMatriculas = matriculas.filter(matricula =>
    matricula.aluno.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
    matricula.curso.nome.toLowerCase().includes(searchTerm.toLowerCase())
  )

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(value)
  }

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('pt-BR')
  }

  const getStatusBadge = (status) => {
    const statusConfig = {
      PENDENTE: { variant: 'destructive', label: 'Pendente' },
      PAGO: { variant: 'default', label: 'Pago' },
      VENCIDO: { variant: 'secondary', label: 'Vencido' }
    }
    
    const config = statusConfig[status] || { variant: 'secondary', label: status }
    return <Badge variant={config.variant}>{config.label}</Badge>
  }

  return (
    <div className="px-4 py-6 sm:px-0">
      <Card>
        <CardHeader>
          <div className="flex justify-between items-center">
            <div>
              <CardTitle>Gerenciamento de Matrículas</CardTitle>
              <CardDescription>
                Gerencie as matrículas dos alunos nos cursos
              </CardDescription>
            </div>
            <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
              <DialogTrigger asChild>
                <Button onClick={resetForm}>
                  <Plus className="w-4 h-4 mr-2" />
                  Nova Matrícula
                </Button>
              </DialogTrigger>
              <DialogContent className="sm:max-w-[425px]">
                <DialogHeader>
                  <DialogTitle>
                    {editingMatricula ? 'Editar Status de Pagamento' : 'Nova Matrícula'}
                  </DialogTitle>
                  <DialogDescription>
                    {editingMatricula 
                      ? 'Altere o status de pagamento da matrícula.' 
                      : 'Preencha as informações para realizar uma nova matrícula.'
                    }
                  </DialogDescription>
                </DialogHeader>
                <form onSubmit={handleSubmit}>
                  <div className="grid gap-4 py-4">
                    {!editingMatricula && (
                      <>
                        <div className="grid grid-cols-4 items-center gap-4">
                          <Label htmlFor="alunoId" className="text-right">
                            Aluno
                          </Label>
                          <Select value={formData.alunoId} onValueChange={(value) => setFormData({...formData, alunoId: value})}>
                            <SelectTrigger className="col-span-3">
                              <SelectValue placeholder="Selecione um aluno" />
                            </SelectTrigger>
                            <SelectContent>
                              {pessoas.map((pessoa) => (
                                <SelectItem key={pessoa.id} value={pessoa.id.toString()}>
                                  {pessoa.nome}
                                </SelectItem>
                              ))}
                            </SelectContent>
                          </Select>
                        </div>
                        <div className="grid grid-cols-4 items-center gap-4">
                          <Label htmlFor="cursoId" className="text-right">
                            Curso
                          </Label>
                          <Select value={formData.cursoId} onValueChange={(value) => setFormData({...formData, cursoId: value})}>
                            <SelectTrigger className="col-span-3">
                              <SelectValue placeholder="Selecione um curso" />
                            </SelectTrigger>
                            <SelectContent>
                              {cursos.map((curso) => (
                                <SelectItem key={curso.id} value={curso.id.toString()}>
                                  {curso.nome} - {formatCurrency(curso.valor)}
                                </SelectItem>
                              ))}
                            </SelectContent>
                          </Select>
                        </div>
                        <div className="grid grid-cols-4 items-center gap-4">
                          <Label htmlFor="valorCobrado" className="text-right">
                            Valor (R$)
                          </Label>
                          <Input
                            id="valorCobrado"
                            type="number"
                            step="0.01"
                            value={formData.valorCobrado}
                            onChange={(e) => setFormData({...formData, valorCobrado: e.target.value})}
                            className="col-span-3"
                            required
                          />
                        </div>
                        <div className="grid grid-cols-4 items-center gap-4">
                          <Label htmlFor="dataVencimento" className="text-right">
                            Vencimento
                          </Label>
                          <Input
                            id="dataVencimento"
                            type="date"
                            value={formData.dataVencimento}
                            onChange={(e) => setFormData({...formData, dataVencimento: e.target.value})}
                            className="col-span-3"
                            required
                          />
                        </div>
                      </>
                    )}
                    
                    {editingMatricula && (
                      <div className="grid grid-cols-4 items-center gap-4">
                        <Label htmlFor="statusPagamento" className="text-right">
                          Status
                        </Label>
                        <Select value={formData.statusPagamento} onValueChange={(value) => setFormData({...formData, statusPagamento: value})}>
                          <SelectTrigger className="col-span-3">
                            <SelectValue placeholder="Selecione o status" />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="PENDENTE">Pendente</SelectItem>
                            <SelectItem value="PAGO">Pago</SelectItem>
                            <SelectItem value="VENCIDO">Vencido</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                    )}
                  </div>
                  <DialogFooter>
                    <Button type="submit">
                      {editingMatricula ? 'Salvar Status' : 'Realizar Matrícula'}
                    </Button>
                  </DialogFooter>
                </form>
              </DialogContent>
            </Dialog>
          </div>
        </CardHeader>
        <CardContent>
          <div className="flex items-center space-x-2 mb-4">
            <Search className="w-4 h-4 text-gray-400" />
            <Input
              placeholder="Buscar por aluno ou curso..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="max-w-sm"
            />
          </div>

          {loading ? (
            <div className="text-center py-4">Carregando...</div>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Aluno</TableHead>
                  <TableHead>Curso</TableHead>
                  <TableHead>Valor</TableHead>
                  <TableHead>Data Matrícula</TableHead>
                  <TableHead>Vencimento</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead className="text-right">Ações</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredMatriculas.map((matricula) => (
                  <TableRow key={matricula.id}>
                    <TableCell className="font-medium">
                      <div className="flex items-center">
                        <UserCheck className="w-4 h-4 mr-2 text-blue-600" />
                        {matricula.aluno.nome}
                      </div>
                    </TableCell>
                    <TableCell>{matricula.curso.nome}</TableCell>
                    <TableCell>{formatCurrency(matricula.valorCobrado)}</TableCell>
                    <TableCell>
                      <div className="flex items-center">
                        <Calendar className="w-4 h-4 mr-2 text-gray-400" />
                        {formatDate(matricula.dataMatricula)}
                      </div>
                    </TableCell>
                    <TableCell>{formatDate(matricula.dataVencimento)}</TableCell>
                    <TableCell>
                      {getStatusBadge(matricula.statusPagamento)}
                    </TableCell>
                    <TableCell className="text-right">
                      <div className="flex justify-end space-x-2">
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => handleEdit(matricula)}
                        >
                          <Edit className="w-4 h-4" />
                        </Button>
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => handleDelete(matricula.id)}
                        >
                          <Trash2 className="w-4 h-4" />
                        </Button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}

          {!loading && filteredMatriculas.length === 0 && (
            <div className="text-center py-4 text-gray-500">
              Nenhuma matrícula encontrada.
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}

export default MatriculasPage

