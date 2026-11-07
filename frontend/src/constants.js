// Shared enum labels so the backend's Java enums and the UI stay in sync.
export const categories = [
  { value: 'ELECTRONICS', label: '电子' },
  { value: 'BOOKS', label: '书籍' },
  { value: 'CLOTHING', label: '衣物' },
  { value: 'KEYS', label: '钥匙' },
  { value: 'DOCUMENTS', label: '证件' },
  { value: 'PETS', label: '宠物' },
  { value: 'OTHERS', label: '其他' }
]

export const categoryLabel = (value) =>
  categories.find((c) => c.value === value)?.label || '其他'

export const typeLabel = (value) => (value === 'LOST' ? '失物' : '招领')

export const statusLabel = (value) => {
  switch (value) {
    case 'OPEN': return '进行中'
    case 'CLAIMED': return '已认领'
    case 'CLOSED': return '已关闭'
    default: return value
  }
}
