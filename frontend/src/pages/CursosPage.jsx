import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button.jsx'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card.jsx'
import { Input } from '@/components/ui/input.jsx'
import { Label } from '@/components/ui/label.jsx'
import { Textarea } from '@/components/ui/textarea.jsx'
import { Switch } from '@/components/ui/switch.jsx'
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog.jsx'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table.jsx'
import { Badge } from '@/components/ui/badge.jsx'
import { Plus, Edit, Trash2, Search, BookOpen } from 'lucide-react'

function CursosPage() {
  const [cursos, setCursos] = useState([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [editingCurso, setEditingCurso] = useState(null)
  const [formData, setFormData] = useState({
    nome: '',
    descricao: '',
    valor: '',
    cargaHoraria: '',
    ativo: true
  })

  useEffect(() => {
    fetchCursos()
  }, [])

  const fetchCursos = async () => {
    try {
      setLoading(true)
      const response = await fetch('http://localhost:8080/api/cursos')
      const data = await response.json()
      setCursos(data)
    } catch (error) {
      console.error('Erro ao buscar cursos:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      const url = editingCurso 
        ? `http://localhost:8080/api/cursos/${editingCurso.id}`
        : 'http://localhost:8080/api/cursos'
      
      const method = editingCurso ? 'PUT' : 'POST'
      
      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          ...formData,
          valor: parseFloat(formData.valor),
          cargaHoraria: parseInt(formData.cargaHoraria)
        }),
      })

      if (response.ok) {
        await fetchCursos()
        setIsDialogOpen(false)
        resetForm()
      } else {
        console.error('Erro ao salvar curso')
      }
    } catch (error) {
      console.error('Erro ao salvar curso:', error)
    }
  }

  const handleEdit = (curso) => {
    setEditingCurso(curso)
    setFormData({
      nome: curso.nome,
      descricao: curso.descricao,
      valor: curso.valor.toString(),
      cargaHoraria: curso.cargaHoraria.toString(),
      ativo: curso.ativo
    })
    setIsDialogOpen(true)
  }

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este curso?')) {
      try {
        const response = await fetch(`http://localhost:8080/api/cursos/${id}`, {
          method: 'DELETE',
        })

        if (response.ok) {
          await fetchCursos()
        } else {
          console.error('Erro ao excluir curso')
        }
      } catch (error) {
        console.error('Erro ao excluir curso:', error)
      }
    }
  }

  const toggleStatus = async (curso) => {
    try {
      const response = await fetch(`http://localhost:8080/api/cursos/${curso.id}/status/${!curso.ativo}`, {
        method: 'PATCH',
      })

      if (response.ok) {
        await fetchCursos()
      } else {
        console.error('Erro ao alterar status do curso')
      }
    } catch (error) {
      console.error('Erro ao alterar status do curso:', error)
    }
  }

  const resetForm = () => {
    setFormData({
      nome: '',
      descricao: '',
      valor: '',
      cargaHoraria: '',
      ativo: true
    })
    setEditingCurso(null)
  }

  const filteredCursos = cursos.filter(curso =>
    curso.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
    curso.descricao.toLowerCase().includes(searchTerm.toLowerCase())
  )

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(value)
  }

  return (
    <div className="px-4 py-6 sm:px-0">
      <Card>
        <CardHeader>
          <div className="flex justify-between items-center">
            <div>
              <CardTitle>Gerenciamento de Cursos</CardTitle>
              <CardDescription>
                Cadastre e gerencie os cursos oferecidos
              </CardDescription>
            </div>
            <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
              <DialogTrigger asChild>
                <Button onClick={resetForm}>
                  <Plus className="w-4 h-4 mr-2" />
                  Novo Curso
                </Button>
              </DialogTrigger>
              <DialogContent className="sm:max-w-[425px]">
                <DialogHeader>
                  <DialogTitle>
                    {editingCurso ? 'Editar Curso' : 'Novo Curso'}
                  </DialogTitle>
                  <DialogDescription>
                    {editingCurso 
                      ? 'Edite as informações do curso.' 
                      : 'Preencha as informações para cadastrar um novo curso.'
                    }
                  </DialogDescription>
                </DialogHeader>
                <form onSubmit={handleSubmit}>
                  <div className="grid gap-4 py-4">
                    <div className="grid grid-cols-4 items-center gap-4">
                      <Label htmlFor="nome" className="text-right">
                        Nome
                      </Label>
                      <Input
                        id="nome"
                        value={formData.nome}
                        onChange={(e) => setFormData({...formData, nome: e.target.value})}
                        className="col-span-3"
                        required
                      />
                    </div>
                    <div className="grid grid-cols-4 items-center gap-4">
                      <Label htmlFor="descricao" className="text-right">
                        Descrição
                      </Label>
                      <Textarea
                        id="descricao"
                        value={formData.descricao}
                        onChange={(e) => setFormData({...formData, descricao: e.target.value})}
                        className="col-span-3"
                        required
                      />
                    </div>
                    <div className="grid grid-cols-4 items-center gap-4">
                      <Label htmlFor="valor" className="text-right">
                        Valor (R$)
                      </Label>
                      <Input
                        id="valor"
                        type="number"
                        step="0.01"
                        value={formData.valor}
                        onChange={(e) => setFormData({...formData, valor: e.target.value})}
                        className="col-span-3"
                        required
                      />
                    </div>
                    <div className="grid grid-cols-4 items-center gap-4">
                      <Label htmlFor="cargaHoraria" className="text-right">
                        Carga Horária
                      </Label>
                      <Input
                        id="cargaHoraria"
                        type="number"
                        value={formData.cargaHoraria}
                        onChange={(e) => setFormData({...formData, cargaHoraria: e.target.value})}
                        className="col-span-3"
                        required
                      />
                    </div>
                    <div className="grid grid-cols-4 items-center gap-4">
                      <Label htmlFor="ativo" className="text-right">
                        Ativo
                      </Label>
                      <Switch
                        id="ativo"
                        checked={formData.ativo}
                        onCheckedChange={(checked) => setFormData({...formData, ativo: checked})}
                      />
                    </div>
                  </div>
                  <DialogFooter>
                    <Button type="submit">
                      {editingCurso ? 'Salvar Alterações' : 'Cadastrar'}
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
              placeholder="Buscar por nome ou descrição..."
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
                  <TableHead>Nome</TableHead>
                  <TableHead>Descrição</TableHead>
                  <TableHead>Valor</TableHead>
                  <TableHead>Carga Horária</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead className="text-right">Ações</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredCursos.map((curso) => (
                  <TableRow key={curso.id}>
                    <TableCell className="font-medium">
                      <div className="flex items-center">
                        <BookOpen className="w-4 h-4 mr-2 text-blue-600" />
                        {curso.nome}
                      </div>
                    </TableCell>
                    <TableCell className="max-w-xs truncate">{curso.descricao}</TableCell>
                    <TableCell>{formatCurrency(curso.valor)}</TableCell>
                    <TableCell>{curso.cargaHoraria}h</TableCell>
                    <TableCell>
                      <Badge 
                        variant={curso.ativo ? "default" : "secondary"}
                        className="cursor-pointer"
                        onClick={() => toggleStatus(curso)}
                      >
                        {curso.ativo ? 'Ativo' : 'Inativo'}
                      </Badge>
                    </TableCell>
                    <TableCell className="text-right">
                      <div className="flex justify-end space-x-2">
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => handleEdit(curso)}
                        >
                          <Edit className="w-4 h-4" />
                        </Button>
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => handleDelete(curso.id)}
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

          {!loading && filteredCursos.length === 0 && (
            <div className="text-center py-4 text-gray-500">
              Nenhum curso encontrado.
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}

export default CursosPage

